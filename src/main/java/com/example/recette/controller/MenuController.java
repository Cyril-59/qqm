package com.example.recette.controller;

import com.example.recette.dto.MenuSelectionRequest;
import com.example.recette.dto.RecetteResponse;
import com.example.recette.service.MenuSelectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@Tag(name = "Menu", description = "Endpoints de gestion de la sélection courante du menu et des courses")
public class MenuController {

    private final MenuSelectionService menuSelectionService;

    public MenuController(MenuSelectionService menuSelectionService) {
        this.menuSelectionService = menuSelectionService;
    }

    @GetMapping
    @Operation(summary = "Récupérer les recettes de la dernière sélection du menu")
    public List<RecetteResponse> getLatestSelection() {
        return menuSelectionService.getLatestSelection();
    }

    @PostMapping
    @Operation(summary = "Enregistrer les recettes sélectionnées lors du calcul des courses")
    public List<RecetteResponse> saveSelection(@RequestBody MenuSelectionRequest request) {
        return menuSelectionService.saveSelection(request);
    }

    @DeleteMapping
    @Operation(summary = "Réinitialiser la sélection du menu")
    public void clearSelection() {
        menuSelectionService.clearSelection();
    }
}
