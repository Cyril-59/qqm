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
        return new ProduitResponse(
                p.getId(),
                p.getLabel(),
                p.getEte(),
                p.getPrintemps(),
                p.getAutomne(),
                p.getHiver(),
                p.getUnite(),
                p.getType(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
