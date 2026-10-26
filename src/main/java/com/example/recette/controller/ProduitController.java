package com.example.recette.controller;

import com.example.recette.dto.ProduitRequest;
import com.example.recette.dto.ProduitResponse;
import com.example.recette.service.ProduitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Produits", description = "Endpoints de gestion des produits et ingrédients bruts")
public class ProduitController {

    public static final String PATH = "/produits";

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping(PATH)
    @Operation(summary = "Lister tous les produits")
    public List<ProduitResponse> getAllProduits() {
        return produitService.getAllProduits();
    }

    @PostMapping(PATH)
    @Operation(summary = "Créer un nouveau produit")
    public ProduitResponse createProduit(@Valid @RequestBody ProduitRequest request) {
        return produitService.createProduit(request);
    }

    @GetMapping(PATH + "/{id}")
    @Operation(summary = "Récupérer un produit par son identifiant")
    public ProduitResponse getProduitById(@PathVariable(value = "id") Long produitId) {
        return produitService.getProduitById(produitId);
    }

    @PutMapping(PATH + "/{id}")
    @Operation(summary = "Mettre à jour un produit existant")
    public ProduitResponse updateProduit(@PathVariable(value = "id") Long produitId,
                                         @Valid @RequestBody ProduitRequest request) {
        return produitService.updateProduit(produitId, request);
    }

    @DeleteMapping(PATH + "/{id}")
    @Operation(summary = "Supprimer un produit par son identifiant")
    public ResponseEntity<Void> deleteProduit(@PathVariable(value = "id") Long produitId) {
        produitService.deleteProduit(produitId);
        return ResponseEntity.ok().build();
    }
}
