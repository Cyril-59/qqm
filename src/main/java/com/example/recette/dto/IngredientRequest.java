package com.example.recette.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IngredientRequest(
        Long id,
        @NotNull(message = "La quantité est obligatoire") Integer quantite,
        ProduitReferenceRequest produit
) {}
