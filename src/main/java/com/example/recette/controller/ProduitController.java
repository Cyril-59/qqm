package com.example.recette.controller;

import com.example.recette.exception.ConstraintViolationException;
import com.example.recette.exception.ResourceNotFoundException;
import com.example.recette.model.Produit;
import com.example.recette.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * Created by Cyril on 18/04/20.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProduitController {

    public static final String PRODUIT = "Produit";
    public static final String PATH = "/produits";
    @Autowired
    ProduitRepository produitRepository;

    @GetMapping(PATH)
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    @PostMapping(PATH)
    public Produit createProduit(@Valid @RequestBody Produit produit) {
        return produitRepository.save(produit);
    }

    @GetMapping(PATH + "/{id}")
    public Produit getProduitById(@PathVariable(value = "id") Long produitId) {
        return produitRepository.findById(produitId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUIT, "id", produitId));
    }

    @PutMapping(PATH + "/{id}")
    public Produit updateProduit(@PathVariable(value = "id") Long produitId,
                                           @Valid @RequestBody Produit produitDetails) {

        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUIT, "id", produitId));

        produit.setLabel(produitDetails.getLabel());
        produit.setEte(produitDetails.getEte());
        produit.setPrintemps(produitDetails.getPrintemps());
        produit.setAutomne(produitDetails.getAutomne());
        produit.setHiver(produitDetails.getHiver());
        produit.setUnite(produitDetails.getUnite());
        produit.setType(produitDetails.getType());

        Produit updatedProduit = produitRepository.save(produit);
        return updatedProduit;
    }

    @DeleteMapping(PATH + "/{id}")
    public ResponseEntity<?> deleteProduit(@PathVariable(value = "id") Long produitId) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUIT, "id", produitId));

        try {
            produitRepository.delete(produit);
        } catch (Exception e) {
            throw new ConstraintViolationException(PRODUIT, "id", produitId);
        }

        return ResponseEntity.ok().build();
    }
}
