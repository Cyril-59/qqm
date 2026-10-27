package com.example.recette.service;

import com.example.recette.dto.IngredientRequest;
import com.example.recette.dto.RecetteRequest;
import com.example.recette.dto.RecetteResponse;
import com.example.recette.exception.ResourceNotFoundException;
import com.example.recette.model.Ingredient;
import com.example.recette.model.Produit;
import com.example.recette.model.Recette;
import com.example.recette.repository.ProduitRepository;
import com.example.recette.repository.RecetteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class RecetteService {

    private static final String RECETTE = "Recette";

    private final RecetteRepository recetteRepository;
    private final ProduitRepository produitRepository;

    public RecetteService(RecetteRepository recetteRepository, ProduitRepository produitRepository) {
        this.recetteRepository = recetteRepository;
        this.produitRepository = produitRepository;
    }

    public List<RecetteResponse> getAllRecettes() {
        return recetteRepository.findAll().stream()
                .map(RecetteResponse::from)
                .toList();
    }

    public RecetteResponse getRecetteById(Long id) {
        return RecetteResponse.from(findRecetteEntity(id));
    }

    public Recette findRecetteEntity(Long id) {
        return recetteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RECETTE, "id", id));
    }

    @Transactional
    public RecetteResponse createRecette(RecetteRequest request) {
        Recette recette = new Recette();
        mapRequestToEntity(request, recette);
        Recette saved = recetteRepository.save(recette);
        if (request.linkedRecetteIds() != null && !request.linkedRecetteIds().isEmpty()) {
            syncReciprocalLinks(saved, request.linkedRecetteIds());
            saved = recetteRepository.save(saved);
        }
        return RecetteResponse.from(saved);
    }

    @Transactional
    public RecetteResponse updateRecette(Long id, RecetteRequest request) {
        Recette recette = findRecetteEntity(id);
        mapRequestToEntity(request, recette);
        syncReciprocalLinks(recette, request.linkedRecetteIds());
        return RecetteResponse.from(recetteRepository.save(recette));
    }

    @Transactional
    public void deleteRecette(Long id) {
        Recette recette = findRecetteEntity(id);
        if (recette.getLinkedRecettes() != null) {
            for (Recette target : new ArrayList<>(recette.getLinkedRecettes())) {
                target.getLinkedRecettes().remove(recette);
            }
            recette.getLinkedRecettes().clear();
        }
        recetteRepository.delete(recette);
    }

    private void syncReciprocalLinks(Recette recette, List<Long> linkedIds) {
        Set<Long> targetIds = linkedIds != null ? new HashSet<>(linkedIds) : Set.of();

        // 1. Dissocier les anciennes recettes retirées
        Set<Recette> currentLinked = new HashSet<>(recette.getLinkedRecettes());
        for (Recette oldLink : currentLinked) {
            if (!targetIds.contains(oldLink.getId())) {
                recette.getLinkedRecettes().remove(oldLink);
                oldLink.getLinkedRecettes().remove(recette);
            }
        }

        // 2. Associer les nouvelles recettes de manière symétrique
        if (!targetIds.isEmpty()) {
            List<Recette> targets = recetteRepository.findAllById(targetIds);
            for (Recette target : targets) {
                if (!target.getId().equals(recette.getId())) {
                    recette.getLinkedRecettes().add(target);
                    target.getLinkedRecettes().add(recette);
                }
            }
        }
    }

    private void mapRequestToEntity(RecetteRequest request, Recette recette) {
        recette.setTitre(request.titre());
        recette.setInstructions(request.instructions());
        if (request.tags() != null) {
            recette.setTags(new java.util.LinkedHashSet<>(request.tags()));
        } else {
            recette.getTags().clear();
        }
        recette.setEte(request.ete());
        recette.setPrintemps(request.printemps());
        recette.setAutomne(request.automne());
        recette.setHiver(request.hiver());

        updateIngredients(recette, request.ingredients());
    }

    private void updateIngredients(Recette recette, List<IngredientRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            recette.getIngredients().clear();
            return;
        }

        Map<Long, Ingredient> existingById = new HashMap<>();
        for (Ingredient ing : recette.getIngredients()) {
            if (ing.getId() != null) {
                existingById.put(ing.getId(), ing);
            }
        }

        Set<Long> retainIds = new HashSet<>();
        List<Ingredient> toAdd = new ArrayList<>();

        for (IngredientRequest ingReq : requests) {
            Produit p = null;
            if (ingReq.produit() != null && ingReq.produit().id() != null) {
                p = produitRepository.findById(ingReq.produit().id()).orElse(null);
            }

            if (ingReq.id() != null && existingById.containsKey(ingReq.id())) {
                Ingredient existing = existingById.get(ingReq.id());
                existing.setQuantite(ingReq.quantite());
                existing.setProduit(p);
                retainIds.add(existing.getId());
            } else {
                Ingredient newIng = new Ingredient();
                newIng.setQuantite(ingReq.quantite());
                newIng.setProduit(p);
                toAdd.add(newIng);
            }
        }

        recette.getIngredients().removeIf(ing -> ing.getId() != null && !retainIds.contains(ing.getId()));
        recette.getIngredients().addAll(toAdd);
    }
}
