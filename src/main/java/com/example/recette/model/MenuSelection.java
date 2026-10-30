package com.example.recette.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "MENU_SELECTION")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true, ignoreUnknown = true)
public class MenuSelection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String coursesText;

    @Column(columnDefinition = "TEXT")
    private String stockJson;

    @Column(columnDefinition = "TEXT")
    private String autresJson;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "menu_selection_done_recettes", joinColumns = @JoinColumn(name = "menu_selection_id"))
    @Column(name = "recette_id")
    private Set<Long> doneRecipeIds = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "menu_selection_recettes",
            joinColumns = @JoinColumn(name = "menu_selection_id"),
            inverseJoinColumns = @JoinColumn(name = "recette_id")
    )
    private List<Recette> recettes = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoursesText() {
        return coursesText;
    }

    public void setCoursesText(String coursesText) {
        this.coursesText = coursesText;
    }

    public String getStockJson() {
        return stockJson;
    }

    public void setStockJson(String stockJson) {
        this.stockJson = stockJson;
    }

    public String getAutresJson() {
        return autresJson;
    }

    public void setAutresJson(String autresJson) {
        this.autresJson = autresJson;
    }

    public Set<Long> getDoneRecipeIds() {
        return doneRecipeIds;
    }

    public void setDoneRecipeIds(Set<Long> doneRecipeIds) {
        this.doneRecipeIds = doneRecipeIds != null ? doneRecipeIds : new HashSet<>();
    }

    public List<Recette> getRecettes() {
        return recettes;
    }

    public void setRecettes(List<Recette> recettes) {
        this.recettes = recettes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
