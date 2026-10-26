package com.example.recette.service;

import com.example.recette.dto.ProduitRequest;
import com.example.recette.dto.ProduitResponse;
import com.example.recette.exception.ConstraintViolationException;
import com.example.recette.exception.ResourceNotFoundException;
import com.example.recette.model.Produit;
import com.example.recette.repository.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProduitService {

    public static final String PRODUIT = "Produit";

    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<ProduitResponse> getAllProduits() {
        return produitRepository.findAll().stream()
                .map(ProduitResponse::from)
                .toList();
    }

    public ProduitResponse getProduitById(Long id) {
        return ProduitResponse.from(findProduitEntity(id));
    }

    public Produit findProduitEntity(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUIT, "id", id));
    }

    @Transactional
    public ProduitResponse createProduit(ProduitRequest request) {
        Produit produit = new Produit();
        mapRequestToEntity(request, produit);
        return ProduitResponse.from(produitRepository.save(produit));
    }

    @Transactional
    public ProduitResponse updateProduit(Long id, ProduitRequest request) {
        Produit produit = findProduitEntity(id);
        mapRequestToEntity(request, produit);
        return ProduitResponse.from(produitRepository.save(produit));
    }

    @Transactional
    public void deleteProduit(Long id) {
        Produit produit = findProduitEntity(id);
        try {
            produitRepository.delete(produit);
            produitRepository.flush();
        } catch (Exception e) {
            throw new ConstraintViolationException(PRODUIT, "id", id);
        }
    }

    private void mapRequestToEntity(ProduitRequest request, Produit produit) {
        produit.setLabel(request.label());
        produit.setEte(request.ete());
        produit.setPrintemps(request.printemps());
        produit.setAutomne(request.automne());
        produit.setHiver(request.hiver());
        produit.setUnite(request.unite());
        produit.setType(request.type());
    }
}
