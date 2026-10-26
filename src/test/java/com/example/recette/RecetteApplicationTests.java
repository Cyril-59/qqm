package com.example.recette;

import com.example.recette.dto.IngredientRequest;
import com.example.recette.dto.ProduitReferenceRequest;
import com.example.recette.dto.ProduitRequest;
import com.example.recette.dto.RecetteRequest;
import com.example.recette.model.Produit;
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
				null, "Tomate", true, true, false, false, "kg", "Légume"
		);

		mockMvc.perform(post("/api/produits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(produitRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.label").value("Tomate"));

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

		IngredientRequest ing = new IngredientRequest(
				null, 250, new ProduitReferenceRequest(savedProduit.getId(), savedProduit.getLabel())
		);

		RecetteRequest r = new RecetteRequest(
				null, "Pâtes nature", false, false, true, true, List.of(ing)
		);

		mockMvc.perform(post("/api/recettes")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(r)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.titre").value("Pâtes nature"))
				.andExpect(jsonPath("$.ingredients[0].quantite").value(250))
				.andExpect(jsonPath("$.ingredients[0].produit.label").value("Pâtes"));

		mockMvc.perform(get("/api/recettes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
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
}
