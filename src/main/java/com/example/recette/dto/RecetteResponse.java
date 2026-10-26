package com.example.recette.dto;

import com.example.recette.model.Recette;

import java.time.Instant;
import java.util.List;

public record RecetteResponse(
        Long id,
        String titre,
        Boolean ete,
        Boolean printemps,
        Boolean automne,
        Boolean hiver,
        List<IngredientResponse> ingredients,
        Instant createdAt,
        Instant updatedAt
) {
    public static RecetteResponse from(Recette r) {
        if (r == null) return null;
        return new RecetteResponse(
                r.getId(),
                r.getTitre(),
                r.getEte(),
                r.getPrintemps(),
                r.getAutomne(),
                r.getHiver(),
                r.getIngredients() != null ? r.getIngredients().stream().map(IngredientResponse::from).toList() : List.of(),
                r.getCreatedAt(),
                r.getUpdatedAt()
        );
    }
}
