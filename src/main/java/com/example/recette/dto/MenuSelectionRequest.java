package com.example.recette.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MenuSelectionRequest(
        List<Long> recetteIds
) {}
