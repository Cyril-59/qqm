package com.example.recette.repository;

import com.example.recette.model.MenuSelection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuSelectionRepository extends JpaRepository<MenuSelection, Long> {

    @EntityGraph(attributePaths = {"recettes"})
    Optional<MenuSelection> findFirstByOrderByIdDesc();

    @EntityGraph(attributePaths = {"recettes"})
    List<MenuSelection> findTop2ByOrderByIdDesc();

    List<MenuSelection> findAllByOrderByIdDesc();
}
