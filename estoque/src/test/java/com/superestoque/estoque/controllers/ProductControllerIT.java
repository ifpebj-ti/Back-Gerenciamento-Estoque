package com.superestoque.estoque.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.superestoque.estoque.token.TokenUtil;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TokenUtil tokenUtil;

	private String adminUsername;
	private String operatorUsername;
	private String password;
	private Long existingId;
	private Long nonExistingId;

	@BeforeEach
	void setUp() throws Exception {
		adminUsername = "alex.brown@ifpe.com";
		operatorUsername = "maria.green@ifpe.com";
		password = "123456";
		existingId = 1L;
		nonExistingId = 1000L;
	}

	@Test
	void findAllProductsPagedByGroupShouldReturnPageWhenAuthenticatedAsAdmin() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		ResultActions result = mockMvc.perform(get("/products").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").isArray());
	}

	@Test
	void findAllProductsPagedByGroupShouldReturnUnauthorizedWhenNoTokenGiven() throws Exception {
		ResultActions result = mockMvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnauthorized());
	}

	@Test
	void findAllProductsPagedByGroupShouldReturnPageWhenAuthenticatedAsOperator() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, password);

		ResultActions result = mockMvc.perform(get("/products").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").isArray());
	}

	@Test
	void findByIdShouldReturnProductWhenIdExists() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId.toString()));
	}

	@Test
	void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
				.header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	void findByIdShouldReturnProductWhenIdExistsAndAuthenticatedAsOperator() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, password);

		ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId.toString()));
	}

	@Test
	void deleteByIdShouldReturnNoContentWhenIdExists() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());
	}

	@Test
	void deleteByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
				.header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	void deleteByIdShouldReturnForbiddenWhenAuthenticatedAsOperator() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, password);

		ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isForbidden());
	}

	@Test
	void saveNewProductShouldCreateProductWhenValidData() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg",
				"fake-image-data".getBytes());

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.multipart("/products").file(photo)
				.param("name", "New Product").param("quantity", "100").param("description", "descrição")
				.param("critical_quantity", "10").param("unit_value", "19.99").param("categories", "1")
				.header("Authorization", "Bearer " + accessToken).contentType(MediaType.MULTIPART_FORM_DATA));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").isNotEmpty());
		result.andExpect(jsonPath("$.name").value("New Product"));
	}

	@Test
	void saveNewProductShouldReturnUnprocessableEntityWhenInvalidData() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg",
				"fake-image-data".getBytes());

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.multipart("/products").file(photo)
				.param("name", "").param("quantity", "-1").param("description", "").param("critical_quantity", "-5")
				.param("unit_value", "0").param("categories", "1").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.MULTIPART_FORM_DATA));

		result.andExpect(status().isUnprocessableEntity());
	}

	@Test
	void saveNewProductShouldReturnForbiddenWhenAuthenticatedAsOperator() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, password);

		MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg",
				"fake-image-data".getBytes());

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.multipart("/products").file(photo)
				.param("name", "New Product").param("quantity", "100").param("critical_quantity", "10")
				.param("description", "descrição").param("unit_value", "19.99").param("categories", "1")
				.header("Authorization", "Bearer " + accessToken).contentType(MediaType.MULTIPART_FORM_DATA));

		result.andExpect(status().isForbidden());
	}

	@Test
	void updateProductShouldUpdateProductWhenValidData() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg",
				"fake-image-data".getBytes());

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.multipart("/products/{id}", existingId)
				.file(photo).param("name", "Updated Product").param("quantity", "200").param("description", "descrição")
				.param("critical_quantity", "20").param("unit_value", "39.99").param("categories", "1")
				.header("Authorization", "Bearer " + accessToken).contentType(MediaType.MULTIPART_FORM_DATA)
				.with(request -> {
					request.setMethod("PUT");
					return request;
				}));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId.toString()));
		result.andExpect(jsonPath("$.name").value("Updated Product"));
	}

	@Test
	void updateProductShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg",
				"fake-image-data".getBytes());

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.multipart("/products/{id}", nonExistingId)
				.file(photo).param("name", "Non-existent Product").param("quantity", "300")
				.param("description", "descrição").param("critical_quantity", "30").param("unit_value", "59.99")
				.param("categories", "1").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.MULTIPART_FORM_DATA).with(request -> {
					request.setMethod("PUT");
					return request;
				}));

		result.andExpect(status().isNotFound());
	}

	@Test
	void updateProductShouldReturnForbiddenWhenAuthenticatedAsOperator() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, password);

		MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg",
				"fake-image-data".getBytes());

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.multipart("/products/{id}", existingId)
				.file(photo).param("name", "Updated Product").param("quantity", "200").param("description", "descrição")
				.param("critical_quantity", "20").param("unit_value", "39.99").param("categories", "1")
				.header("Authorization", "Bearer " + accessToken).contentType(MediaType.MULTIPART_FORM_DATA)
				.with(request -> {
					request.setMethod("PUT");
					return request;
				}));

		result.andExpect(status().isForbidden());
	}

}