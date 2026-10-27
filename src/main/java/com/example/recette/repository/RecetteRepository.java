package com.example.recette.repository;

import com.example.recette.model.Recette;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Cyril on 18/04/20.
 */
@Repository
public interface RecetteRepository extends JpaRepository<Recette, Long> {

    @EntityGraph(attributePaths = {"ingredients", "ingredients.produit", "tags", "linkedRecettes"})
    @Override
    List<Recette> findAll();

    @EntityGraph(attributePaths = {"ingredients", "ingredients.produit", "tags", "linkedRecettes"})
    @Override
    Optional<Recette> findById(Long id);
}
