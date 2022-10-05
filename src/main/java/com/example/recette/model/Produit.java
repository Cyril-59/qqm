package com.example.recette.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

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
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
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
