package com.example.recette.controller;

import com.example.recette.exception.ResourceNotFoundException;
import com.example.recette.model.Ingredient;
import com.example.recette.model.Produit;
import com.example.recette.model.Recette;
import com.example.recette.repository.ProduitRepository;
import com.example.recette.repository.RecetteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Cyril on 18/04/20.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class RecetteController {

    public static final String RECETTE = "Recette";
    public static final String PATH = "/recettes";
    @Autowired
    RecetteRepository recetteRepository;
    @Autowired
    ProduitRepository produitRepository;

    @GetMapping(PATH)
    public List<Recette> getAllRecettes() {
        return recetteRepository.findAll();
    }

    @PostMapping(PATH)
    public Recette createRecette(@Valid @RequestBody Recette recette) {
        return recetteRepository.save(recette);
    }

    @GetMapping(PATH + "/{id}")
    public Recette getRecetteById(@PathVariable(value = "id") Long recetteId) {
        return recetteRepository.findById(recetteId)
                .orElseThrow(() -> new ResourceNotFoundException(RECETTE, "id", recetteId));
    }

    @PutMapping(PATH + "/{id}")
    public Recette updateRecette(@PathVariable(value = "id") Long recetteId,
                                           @Valid @RequestBody Recette recetteDetails) {

        recetteRepository.findById(recetteId)
                .orElseThrow(() -> new ResourceNotFoundException(RECETTE, "id", recetteId));

        return recetteRepository.save(recetteDetails);
    }

    @DeleteMapping(PATH + "/{id}")
    public ResponseEntity<?> deleteRecette(@PathVariable(value = "id") Long recetteId) {
        Recette recette = recetteRepository.findById(recetteId)
                .orElseThrow(() -> new ResourceNotFoundException(RECETTE, "id", recetteId));

        recetteRepository.delete(recette);

        return ResponseEntity.ok().build();
    }
}
