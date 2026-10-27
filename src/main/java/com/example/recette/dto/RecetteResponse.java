package com.example.recette.dto;

import com.example.recette.model.Recette;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public record RecetteResponse(
        Long id,
        String titre,
        String instructions,
        Set<String> tags,
        List<RecetteSummaryResponse> linkedRecettes,
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
                r.getInstructions(),
                r.getTags() != null ? r.getTags() : Set.of(),
                r.getLinkedRecettes() != null ? r.getLinkedRecettes().stream().map(RecetteSummaryResponse::from).toList() : List.of(),
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
