package com.example.recette.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProduitRequest(
        Long id,
        @NotBlank(message = "Le label est obligatoire") String label,
        Boolean ete,
        Boolean printemps,
        Boolean automne,
        Boolean hiver,
        @NotBlank(message = "L'unité est obligatoire") String unite,
        @NotBlank(message = "Le type est obligatoire") String type
) {}
