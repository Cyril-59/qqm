package com.example.recette.dto;

import java.time.Instant;
import java.util.List;

public record MenuSelectionHistoryResponse(
        Long id,
        Instant createdAt,
        String coursesText,
        String stockJson,
        String autresJson,
        List<RecetteResponse> recettes
) {}
