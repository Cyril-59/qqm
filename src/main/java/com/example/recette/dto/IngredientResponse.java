package com.example.recette.dto;

import com.example.recette.model.Ingredient;

import java.time.Instant;

public record IngredientResponse(
        Long id,
        Integer quantite,
        ProduitResponse produit,
        Instant createdAt,
        Instant updatedAt
) {
    public static IngredientResponse from(Ingredient i) {
        if (i == null) return null;
        return new IngredientResponse(
                i.getId(),
                i.getQuantite(),
                ProduitResponse.from(i.getProduit()),
                i.getCreatedAt(),
                i.getUpdatedAt()
        );
    }
}
