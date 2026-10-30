package com.example.recette.dto;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public record MenuSelectionHistoryResponse(
        Long id,
        Instant createdAt,
        String coursesText,
        String stockJson,
        String autresJson,
        Set<Long> doneRecipeIds,
        List<RecetteResponse> recettes
) {}
