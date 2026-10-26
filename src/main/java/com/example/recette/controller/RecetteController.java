package com.example.recette.controller;

import com.example.recette.dto.RecetteRequest;
import com.example.recette.dto.RecetteResponse;
import com.example.recette.service.RecetteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Recettes", description = "Endpoints de gestion des recettes de cuisine")
public class RecetteController {

    public static final String PATH = "/recettes";

    private final RecetteService recetteService;

    public RecetteController(RecetteService recetteService) {
        this.recetteService = recetteService;
    }

    @GetMapping(PATH)
    @Operation(summary = "Lister toutes les recettes avec leurs ingrédients")
    public List<RecetteResponse> getAllRecettes() {
        return recetteService.getAllRecettes();
    }

    @PostMapping(PATH)
    @Operation(summary = "Créer une nouvelle recette")
    public RecetteResponse createRecette(@Valid @RequestBody RecetteRequest request) {
        return recetteService.createRecette(request);
    }

    @GetMapping(PATH + "/{id}")
    @Operation(summary = "Récupérer une recette par son identifiant")
    public RecetteResponse getRecetteById(@PathVariable(value = "id") Long recetteId) {
        return recetteService.getRecetteById(recetteId);
    }

    @PutMapping(PATH + "/{id}")
    @Operation(summary = "Mettre à jour une recette existante")
    public RecetteResponse updateRecette(@PathVariable(value = "id") Long recetteId,
                                         @Valid @RequestBody RecetteRequest request) {
        return recetteService.updateRecette(recetteId, request);
    }

    @DeleteMapping(PATH + "/{id}")
    @Operation(summary = "Supprimer une recette par son identifiant")
    public ResponseEntity<Void> deleteRecette(@PathVariable(value = "id") Long recetteId) {
        recetteService.deleteRecette(recetteId);
        return ResponseEntity.ok().build();
    }
}
