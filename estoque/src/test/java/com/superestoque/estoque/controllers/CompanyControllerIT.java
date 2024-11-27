package com.superestoque.estoque.controllers;

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
public class CompanyControllerIT {

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
	public void getMyCompanyShouldReturnCompanyWhenAdminAuthenticated() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		ResultActions result = mockMvc.perform(get("/company").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").isNotEmpty());
		result.andExpect(jsonPath("$.name").isNotEmpty());
	}

	@Test
	public void getMyCompanyShouldReturnUnauthorizedWhenNoTokenGiven() throws Exception {
		ResultActions result = mockMvc.perform(get("/company").contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnauthorized());
	}

	@Test
	public void getMyCompanyShouldReturnForbiddenWhenOperatorAuthenticated() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, password);

		ResultActions result = mockMvc.perform(get("/company").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isForbidden());
	}

	@Test
	public void updateCompanyShouldUpdateCompanyWhenValidData() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg",
				"fake-image-data".getBytes());

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.multipart("/company/{id}", existingId).file(photo)
				.param("name", "New Company Name").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.MULTIPART_FORM_DATA).with(request -> {
					request.setMethod("PUT");
					return request;
				}));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").isNotEmpty());
		result.andExpect(jsonPath("$.name").value("New Company Name"));
	}

	@Test
	public void updateCompanyShouldReturnNotFoundWhenNonExistingId() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg",
				"fake-image-data".getBytes());

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.multipart("/company/{id}", nonExistingId)
				.file(photo).param("name", "Non-Existent Company").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.MULTIPART_FORM_DATA).with(request -> {
					request.setMethod("PUT");
					return request;
				}));

		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.message").value("Empresa não encontrada."));
	}

	@Test
	public void updateCompanyShouldReturnForbiddenWhenOperatorAuthenticated() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, password);

		MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg",
				"fake-image-data".getBytes());

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.multipart("/company/{id}", existingId).file(photo)
				.param("name", "Updated Company Name").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.MULTIPART_FORM_DATA).with(request -> {
					request.setMethod("PUT");
					return request;
				}));

		result.andExpect(status().isForbidden());
	}

	@Test
	public void getUserMyCompanyShouldReturnListOfUsersWhenAdminAuthenticated() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		ResultActions result = mockMvc.perform(get("/company/users").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$").isArray());
		result.andExpect(jsonPath("$[0].id").isNotEmpty());
		result.andExpect(jsonPath("$[0].name").isNotEmpty());
	}

	@Test
	public void getUserMyCompanyShouldReturnUnauthorizedWhenNoTokenGiven() throws Exception {
		ResultActions result = mockMvc.perform(get("/company/users").contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnauthorized());
	}

	@Test
	public void getUserMyCompanyShouldReturnForbiddenWhenOperatorAuthenticated() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, password);

		ResultActions result = mockMvc.perform(get("/company/users").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isForbidden());
	}
}