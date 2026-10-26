package com.example.recette.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

/**
 * Created by Cyril on 18/04/20.
 */
@Entity
@Table(name = "PRODUIT")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true, ignoreUnknown=true)
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String label;

    private Boolean ete;
    private Boolean printemps;
    private Boolean automne;
    private Boolean hiver;

    @NotBlank
    private String unite;

    @NotBlank
    private String type;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getEte() {
        return ete;
    }

    public void setEte(Boolean ete) {
        this.ete = ete;
    }

    public Boolean getPrintemps() {
        return printemps;
    }

    public void setPrintemps(Boolean printemps) {
        this.printemps = printemps;
    }

    public Boolean getAutomne() {
        return automne;
    }

    public void setAutomne(Boolean automne) {
        this.automne = automne;
    }

    public Boolean getHiver() {
        return hiver;
    }

    public void setHiver(Boolean hiver) {
        this.hiver = hiver;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
