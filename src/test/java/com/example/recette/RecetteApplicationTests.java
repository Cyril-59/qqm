package com.example.recette;

import com.example.recette.dto.IngredientRequest;
import com.example.recette.dto.ProduitReferenceRequest;
import com.example.recette.dto.ProduitRequest;
import com.example.recette.dto.RecetteRequest;
import com.example.recette.dto.RecetteResponse;
import com.example.recette.model.Produit;
import com.example.recette.model.Recette;
import com.example.recette.repository.ProduitRepository;
import com.example.recette.repository.RecetteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecetteApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProduitRepository produitRepository;

	@Autowired
	private RecetteRepository recetteRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void testIndexEndpoint() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string("Hello, use /api/apiName"));
	}

	@Test
	void testCreateAndGetProduit() throws Exception {
		ProduitRequest produitRequest = new ProduitRequest(
				null, "Tomate", true, true, false, false, "kg", "épicerie vrac"
		);

		mockMvc.perform(post("/api/produits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(produitRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.label").value("Tomate"))
				.andExpect(jsonPath("$.type").value("Vrac"));

		mockMvc.perform(get("/api/produits"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}

	@Test
	void testCreateAndGetRecetteWithIngredients() throws Exception {
		Produit p = new Produit();
		p.setLabel("Pâtes");
		p.setUnite("g");
		p.setType("Épicerie");
		Produit savedProduit = produitRepository.save(p);

		Recette r1 = new Recette();
		r1.setTitre("Sauce tomate");
		Recette savedLinked = recetteRepository.save(r1);

		IngredientRequest ing = new IngredientRequest(
				null, 250, new ProduitReferenceRequest(savedProduit.getId(), savedProduit.getLabel())
		);

		RecetteRequest r = new RecetteRequest(
				null, "Pâtes nature", "Faire bouillir l'eau salée, cuire 10 min.", java.util.Set.of("Végétarien", "Express"), List.of(savedLinked.getId()), false, false, true, true, List.of(ing)
		);

		mockMvc.perform(post("/api/recettes")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(r)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.titre").value("Pâtes nature"))
				.andExpect(jsonPath("$.instructions").value("Faire bouillir l'eau salée, cuire 10 min."))
				.andExpect(jsonPath("$.tags[0]").exists())
				.andExpect(jsonPath("$.linkedRecettes[0].id").value(savedLinked.getId()))
				.andExpect(jsonPath("$.ingredients[0].quantite").value(250))
				.andExpect(jsonPath("$.ingredients[0].produit.label").value("Pâtes"));

		// Vérifier que la recette liée réciproquement possède bien le lien inverse
		mockMvc.perform(get("/api/recettes/" + savedLinked.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.linkedRecettes[0].titre").value("Pâtes nature"));

		mockMvc.perform(get("/api/recettes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}

	@Test
	void testUpdateRecetteWithExistingIngredientsAndLinks() throws Exception {
		Produit p = new Produit();
		p.setLabel("Lait de coco");
		p.setUnite("cl");
		p.setType("Épicerie");
		Produit savedProduit = produitRepository.save(p);

		Recette rPartner = new Recette();
		rPartner.setTitre("Curry de légumes");
		Recette savedPartner = recetteRepository.save(rPartner);

		// 1. Créer une recette avec ingrédient
		IngredientRequest ing = new IngredientRequest(
				null, 20, new ProduitReferenceRequest(savedProduit.getId(), savedProduit.getLabel())
		);
		RecetteRequest createReq = new RecetteRequest(
				null, "Dahl de lentilles", "Cuire 20 min.", java.util.Set.of("Végétarien"), List.of(), false, false, true, true, List.of(ing)
		);
		String res = mockMvc.perform(post("/api/recettes")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createReq)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		RecetteResponse created = objectMapper.readValue(res, RecetteResponse.class);
		Long createdId = created.id();
		Long createdIngId = created.ingredients().get(0).id();

		// 2. Mettre à jour avec l'ID d'ingrédient existant et ajouter la liaison anti-gaspillage
		IngredientRequest updateIng = new IngredientRequest(
				createdIngId, 25, new ProduitReferenceRequest(savedProduit.getId(), savedProduit.getLabel())
		);
		RecetteRequest updateReq = new RecetteRequest(
				createdId, "Dahl de lentilles au coco", "Cuire 25 min.", java.util.Set.of("Végétarien"),
				List.of(savedPartner.getId()), false, false, true, true, List.of(updateIng)
		);

		mockMvc.perform(put("/api/recettes/" + createdId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateReq)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(createdId))
				.andExpect(jsonPath("$.titre").value("Dahl de lentilles au coco"))
				.andExpect(jsonPath("$.ingredients[0].quantite").value(25))
				.andExpect(jsonPath("$.linkedRecettes[0].id").value(savedPartner.getId()));
	}

	@Test
	void testNotFoundProblemDetail() throws Exception {
		mockMvc.perform(get("/api/produits/999999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.title").value("Resource Not Found"))
				.andExpect(jsonPath("$.resourceName").value("Produit"));
	}

	@Test
	void testValidationErrorProblemDetail() throws Exception {
		ProduitRequest invalidRequest = new ProduitRequest(null, "", null, null, null, null, "", "");

		mockMvc.perform(post("/api/produits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.title").value("Validation Error"))
				.andExpect(jsonPath("$.errors").exists());
	}

	@Test
	void testCorsHeaders() throws Exception {
		mockMvc.perform(get("/api/produits")
						.header("Origin", "http://localhost:4200"))
				.andExpect(status().isOk())
				.andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));

		mockMvc.perform(get("/api/produits")
						.header("Origin", "https://recette-lac.vercel.app"))
				.andExpect(status().isOk())
				.andExpect(header().string("Access-Control-Allow-Origin", "https://recette-lac.vercel.app"));

		mockMvc.perform(get("/api/produits")
						.header("Origin", "https://preview-deploy.vercel.app"))
				.andExpect(status().isOk())
				.andExpect(header().string("Access-Control-Allow-Origin", "https://preview-deploy.vercel.app"));
	}

	@Test
	void testOpenApiDocsEndpoint() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi").exists())
				.andExpect(jsonPath("$.info.title").value("API Recette"));
	}

	@Test
	void testMenuSelection() throws Exception {
		Recette r = new Recette();
		r.setTitre("Gratin dauphinois");
		Recette savedRecette = recetteRepository.save(r);

		// 1. Initialement vide ou existant
		mockMvc.perform(get("/api/menu"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());

		// 2. Enregistrement d'une sélection de menu
		mockMvc.perform(post("/api/menu")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new com.example.recette.dto.MenuSelectionRequest(List.of(savedRecette.getId())))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].id").value(savedRecette.getId()))
				.andExpect(jsonPath("$[0].titre").value("Gratin dauphinois"));

		// 3. Récupération de la dernière sélection
		mockMvc.perform(get("/api/menu"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(savedRecette.getId()))
				.andExpect(jsonPath("$[0].titre").value("Gratin dauphinois"));

		// 4. Récupération de l'historique (GET /api/menu/history)
		mockMvc.perform(get("/api/menu/history"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].recettes[0].id").value(savedRecette.getId()));

		// 5. Sauvegarder d'autres sélections pour vérifier qu'on conserve au maximum 2 occurrences
		Recette r2 = new Recette();
		r2.setTitre("Tarte aux pommes");
		Recette saved2 = recetteRepository.save(r2);
		mockMvc.perform(post("/api/menu")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new com.example.recette.dto.MenuSelectionRequest(List.of(saved2.getId())))))
				.andExpect(status().isOk());

		Recette r3 = new Recette();
		r3.setTitre("Soupe poireau");
		Recette saved3 = recetteRepository.save(r3);
		mockMvc.perform(post("/api/menu")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new com.example.recette.dto.MenuSelectionRequest(List.of(saved3.getId())))))
				.andExpect(status().isOk());

		// L'historique ne doit contenir que 2 occurrences
		mockMvc.perform(get("/api/menu/history"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].recettes[0].titre").value("Soupe poireau"))
				.andExpect(jsonPath("$[1].recettes[0].titre").value("Tarte aux pommes"));
	}
}
