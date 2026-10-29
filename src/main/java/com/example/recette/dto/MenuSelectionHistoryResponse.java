package com.example.recette.dto;

import java.time.Instant;
import java.util.List;

public record MenuSelectionHistoryResponse(
        Long id,
        Instant createdAt,
        List<RecetteResponse> recettes
) {}
