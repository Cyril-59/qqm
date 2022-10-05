package com.example.recette.repository;

import com.example.recette.model.Produit;
import com.example.recette.model.Recette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Cyril on 18/04/20.
 */

@Repository
public interface RecetteRepository extends JpaRepository<Recette, Long> {

}
