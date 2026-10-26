package com.example.recette.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProduitReferenceRequest(
        Long id,
        String label
) {}
