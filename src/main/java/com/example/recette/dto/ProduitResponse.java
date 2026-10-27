package com.example.recette.dto;

import com.example.recette.model.Produit;

import java.time.Instant;

public record ProduitResponse(
        Long id,
        String label,
        Boolean ete,
        Boolean printemps,
        Boolean automne,
        Boolean hiver,
        String unite,
        String type,
        Instant createdAt,
        Instant updatedAt
) {
    public static ProduitResponse from(Produit p) {
        if (p == null) return null;
        String type = p.getType();
        if (type != null) {
            String trimmed = type.trim();
            if ("épicerie vrac".equalsIgnoreCase(trimmed) || "vrac".equalsIgnoreCase(trimmed)) {
                type = "Vrac";
            } else if ("légume".equalsIgnoreCase(trimmed) || "legume".equalsIgnoreCase(trimmed)) {
                type = "Légume";
            } else if ("frais".equalsIgnoreCase(trimmed)) {
                type = "Frais";
            } else if ("épicerie".equalsIgnoreCase(trimmed) || "epicerie".equalsIgnoreCase(trimmed)) {
                type = "Épicerie";
            } else if (!trimmed.isEmpty()) {
                type = Character.toUpperCase(trimmed.charAt(0)) + trimmed.substring(1);
            }
        }
        return new ProduitResponse(
                p.getId(),
                p.getLabel(),
                p.getEte(),
                p.getPrintemps(),
                p.getAutomne(),
                p.getHiver(),
                p.getUnite(),
                type,
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
