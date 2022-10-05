package com.example.recette.repository;

import com.example.recette.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Cyril on 18/04/20.
 */

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

}
