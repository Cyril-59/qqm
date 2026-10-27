package com.example.recette.service;

import com.example.recette.dto.MenuSelectionRequest;
import com.example.recette.dto.RecetteResponse;
import com.example.recette.model.MenuSelection;
import com.example.recette.model.Recette;
import com.example.recette.repository.MenuSelectionRepository;
import com.example.recette.repository.RecetteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MenuSelectionService {

    private final MenuSelectionRepository menuSelectionRepository;
    private final RecetteRepository recetteRepository;

    public MenuSelectionService(MenuSelectionRepository menuSelectionRepository, RecetteRepository recetteRepository) {
        this.menuSelectionRepository = menuSelectionRepository;
        this.recetteRepository = recetteRepository;
    }

    public List<RecetteResponse> getLatestSelection() {
        Optional<MenuSelection> menuOpt = menuSelectionRepository.findFirstByOrderByIdDesc();
        if (menuOpt.isEmpty() || menuOpt.get().getRecettes() == null || menuOpt.get().getRecettes().isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> selectedIds = menuOpt.get().getRecettes().stream()
                .map(Recette::getId)
                .toList();

        // Use recetteRepository.findAll() which already has @EntityGraph for ingredients and produits
        return recetteRepository.findAll().stream()
                .filter(r -> selectedIds.contains(r.getId()))
                .map(RecetteResponse::from)
                .toList();
    }

    @Transactional
    public List<RecetteResponse> saveSelection(MenuSelectionRequest request) {
        MenuSelection menuSelection = new MenuSelection();
        if (request != null && request.recetteIds() != null && !request.recetteIds().isEmpty()) {
            List<Recette> recettes = recetteRepository.findAllById(request.recetteIds());
            menuSelection.setRecettes(recettes);
        }
        menuSelectionRepository.save(menuSelection);
        return getLatestSelection();
    }

    @Transactional
    public void clearSelection() {
        menuSelectionRepository.deleteAll();
    }
}
