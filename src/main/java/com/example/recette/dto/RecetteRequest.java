package com.example.recette.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RecetteRequest(
        Long id,
        @NotBlank(message = "Le titre est obligatoire") String titre,
        Boolean ete,
        Boolean printemps,
        Boolean automne,
        Boolean hiver,
        List<IngredientRequest> ingredients
) {}
