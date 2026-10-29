package com.example.recette.dto;

import java.util.List;

public record MenuSelectionRequest(
        List<Long> recetteIds,
        String coursesText,
        String stockJson,
        String autresJson
) {
    public MenuSelectionRequest(List<Long> recetteIds) {
        this(recetteIds, null, null, null);
    }
}
