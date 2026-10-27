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

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RecetteService {

    public static final String RECETTE = "Recette";

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
        return RecetteResponse.from(recetteRepository.save(recette));
    }

    @Transactional
    public RecetteResponse updateRecette(Long id, RecetteRequest request) {
        Recette recette = findRecetteEntity(id);
        mapRequestToEntity(request, recette);
        return RecetteResponse.from(recetteRepository.save(recette));
    }

    @Transactional
    public void deleteRecette(Long id) {
        Recette recette = findRecetteEntity(id);
        recetteRepository.delete(recette);
    }

    private void mapRequestToEntity(RecetteRequest request, Recette recette) {
        recette.setTitre(request.titre());
        recette.setInstructions(request.instructions());
        if (request.tags() != null) {
            recette.setTags(new java.util.LinkedHashSet<>(request.tags()));
        } else {
            recette.getTags().clear();
        }
        if (request.linkedRecetteIds() != null && !request.linkedRecetteIds().isEmpty()) {
            List<Recette> linked = recetteRepository.findAllById(request.linkedRecetteIds());
            recette.setLinkedRecettes(new java.util.LinkedHashSet<>(linked));
        } else {
            recette.getLinkedRecettes().clear();
        }
        recette.setEte(request.ete());
        recette.setPrintemps(request.printemps());
        recette.setAutomne(request.automne());
        recette.setHiver(request.hiver());

        List<Ingredient> ingredients = new ArrayList<>();
        if (request.ingredients() != null) {
            for (IngredientRequest ingReq : request.ingredients()) {
                Ingredient ing = new Ingredient();
                if (ingReq.id() != null) {
                    ing.setId(ingReq.id());
                }
                ing.setQuantite(ingReq.quantite());
                if (ingReq.produit() != null && ingReq.produit().id() != null) {
                    Produit p = produitRepository.findById(ingReq.produit().id()).orElse(null);
                    ing.setProduit(p);
                }
                ingredients.add(ing);
            }
        }
        recette.getIngredients().clear();
        recette.getIngredients().addAll(ingredients);
    }
}
