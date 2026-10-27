package com.example.recette.dto;

import com.example.recette.model.Recette;

public record RecetteSummaryResponse(
        Long id,
        String titre
) {
    public static RecetteSummaryResponse from(Recette r) {
        if (r == null) return null;
        return new RecetteSummaryResponse(r.getId(), r.getTitre());
    }
}
