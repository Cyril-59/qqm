package com.example.recette.dto;

import com.example.recette.model.Ingredient;
import com.example.recette.model.Recette;

import java.time.Instant;
import java.util.*;

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

        List<IngredientResponse> uniqueIngredients = List.of();
        if (r.getIngredients() != null && !r.getIngredients().isEmpty()) {
            Map<Long, IngredientResponse> seen = new LinkedHashMap<>();
            for (Ingredient ing : r.getIngredients()) {
                if (ing != null) {
                    Long key = ing.getId() != null ? ing.getId() : (ing.getProduit() != null ? ing.getProduit().getId() : null);
                    if (key != null) {
                        seen.putIfAbsent(key, IngredientResponse.from(ing));
                    } else {
                        seen.put((long) -seen.size(), IngredientResponse.from(ing));
                    }
                }
            }
            uniqueIngredients = new ArrayList<>(seen.values());
        }

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
                uniqueIngredients,
                r.getCreatedAt(),
                r.getUpdatedAt()
        );
    }
}
