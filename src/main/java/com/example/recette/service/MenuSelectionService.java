package com.example.recette.service;

import com.example.recette.dto.MenuSelectionHistoryResponse;
import com.example.recette.dto.MenuSelectionRequest;
import com.example.recette.dto.RecetteResponse;
import com.example.recette.model.MenuSelection;
import com.example.recette.model.Recette;
import com.example.recette.repository.MenuSelectionRepository;
import com.example.recette.repository.RecetteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

        return recetteRepository.findAll().stream()
                .filter(r -> selectedIds.contains(r.getId()))
                .distinct()
                .map(RecetteResponse::from)
                .toList();
    }

    public List<MenuSelectionHistoryResponse> getHistory() {
        List<MenuSelection> top2 = menuSelectionRepository.findTop2ByOrderByIdDesc();
        if (top2.isEmpty()) {
            return Collections.emptyList();
        }

        List<RecetteResponse> allRecettes = recetteRepository.findAll().stream()
                .map(RecetteResponse::from)
                .toList();
        Map<Long, RecetteResponse> recetteMap = allRecettes.stream()
                .collect(Collectors.toMap(RecetteResponse::id, r -> r, (r1, r2) -> r1));

        return top2.stream().map(ms -> {
            List<RecetteResponse> list = ms.getRecettes() != null ? ms.getRecettes().stream()
                    .map(r -> recetteMap.get(r.getId()))
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList() : List.of();
            return new MenuSelectionHistoryResponse(
                    ms.getId(),
                    ms.getCreatedAt(),
                    ms.getCoursesText(),
                    ms.getStockJson(),
                    ms.getAutresJson(),
                    list
            );
        }).toList();
    }

    @Transactional
    public List<RecetteResponse> saveSelection(MenuSelectionRequest request) {
        MenuSelection menuSelection = new MenuSelection();
        if (request != null) {
            menuSelection.setCoursesText(request.coursesText());
            menuSelection.setStockJson(request.stockJson());
            menuSelection.setAutresJson(request.autresJson());
            if (request.recetteIds() != null && !request.recetteIds().isEmpty()) {
                List<Long> distinctIds = request.recetteIds().stream().distinct().toList();
                List<Recette> recettes = recetteRepository.findAllById(distinctIds);
                menuSelection.setRecettes(recettes);
            }
        }
        menuSelectionRepository.save(menuSelection);

        // Retenir uniquement les 2 dernières occurrences pour conserver l'historique de la semaine précédente
        List<MenuSelection> all = menuSelectionRepository.findAllByOrderByIdDesc();
        if (all.size() > 2) {
            for (int i = 2; i < all.size(); i++) {
                menuSelectionRepository.delete(all.get(i));
            }
        }

        return getLatestSelection();
    }

    @Transactional
    public void clearSelection() {
        menuSelectionRepository.deleteAll();
    }
}
