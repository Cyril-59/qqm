package com.example.recette.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RecetteRequest(
        Long id,
        @NotBlank(message = "Le titre est obligatoire") String titre,
        String instructions,
        Set<String> tags,
        List<Long> linkedRecetteIds,
        Boolean ete,
        Boolean printemps,
        Boolean automne,
        Boolean hiver,
        List<IngredientRequest> ingredients
) {}
