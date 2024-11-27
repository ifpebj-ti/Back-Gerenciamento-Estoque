package com.superestoque.estoque.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.superestoque.estoque.entities.dto.RoleDTO;
import com.superestoque.estoque.entities.dto.UserInsertDTO;
import com.superestoque.estoque.entities.dto.UserUpdatePasswordDTO;
import com.superestoque.estoque.token.TokenUtil;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TokenUtil tokenUtil;

	@Autowired
	private ObjectMapper objectMapper;

	private String adminUsername;
	private String operatorUsername;
	private String password;
	private Long existingId;
	private Long nonExistingId;
	private UserInsertDTO userInsert;

	@BeforeEach
	void setUp() throws Exception {
		adminUsername = "alex.brown@ifpe.com";
		operatorUsername = "maria.green@ifpe.com";
		password = "123456";
		existingId = 1L;
		nonExistingId = 1000L;
		userInsert = new UserInsertDTO(5L, "Usuário teste", "teste@ifpe.com", true, "123456789");
		userInsert.getRoles().add(new RoleDTO(2L, "ROLE_OPERATOR"));
	}

	@Test
	public void findUserByIdShouldReturnUserWhenAuthenticatedAsAdmin() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		ResultActions result = mockMvc.perform(get("/users/me").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").isNotEmpty());
		result.andExpect(jsonPath("$.email").isNotEmpty());
	}

	@Test
	public void findUserByIdShouldReturnUnauthorizedWhenNoTokenProvided() throws Exception {
		ResultActions result = mockMvc.perform(get("/users/me").contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnauthorized());
	}

	@Test
	public void insertShouldReturnUnauthorizedWhenNotValidToken() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(userInsert);

		ResultActions result = mockMvc.perform(post("/users").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnauthorized());
	}

	@Test
	public void saveNewUserShouldReturnUnprocessableEntityWhenInvalidData() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);
		
		userInsert = new UserInsertDTO(78L, "Usuário teste", "", true, "123456789");

		String jsonBody = objectMapper.writeValueAsString(userInsert);

		ResultActions result = mockMvc.perform(post("/users").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON).content(jsonBody));

		result.andExpect(status().isUnprocessableEntity());
		result.andExpect(jsonPath("$.errors").isArray());
	}

	@Test
	public void saveNewUserShouldReturnForbiddenWhenOperatorAuthenticated() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, password);

		String jsonBody = objectMapper.writeValueAsString(userInsert);

		ResultActions result = mockMvc.perform(post("/users").header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON).content(jsonBody));

		result.andExpect(status().isForbidden());
	}

	@Test
	public void desactivateUserShouldReturnNoContentWhenAdmin() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		ResultActions result = mockMvc.perform(put("/users/desactive/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());
	}

	@Test
	public void desactivateUserShouldReturnNotFoundWhenNonExistingId() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		ResultActions result = mockMvc.perform(put("/users/desactive/{id}", nonExistingId)
				.header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.message").value("Usuário não encontrado."));
	}

	@Test
	public void desactivateUserShouldReturnForbiddenWhenOperatorAuthenticated() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, password);

		ResultActions result = mockMvc.perform(put("/users/desactive/{id}", existingId)
				.header("Authorization", "Bearer " + accessToken).contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isForbidden());
	}

	@Test
	public void updatePasswordShouldReturnNoContentWhenValid() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		UserUpdatePasswordDTO updatePasswordDTO = new UserUpdatePasswordDTO("newPassword123");

		String jsonBody = objectMapper.writeValueAsString(updatePasswordDTO);

		ResultActions result = mockMvc
				.perform(put("/users/updatePassword/{id}", existingId).header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(jsonBody));

		result.andExpect(status().isNoContent());
	}

	@Test
	public void updatePasswordShouldReturnBadRequestWhenInvalidData() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		UserUpdatePasswordDTO updatePasswordDTO = new UserUpdatePasswordDTO("");

		String jsonBody = objectMapper.writeValueAsString(updatePasswordDTO);

		ResultActions result = mockMvc
				.perform(put("/users/updatePassword/{id}", existingId).header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(jsonBody));

		result.andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void updateRoleShouldReturnNoContentWhenValid() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, password);

		RoleDTO roleDTO = new RoleDTO(1L, "ROLE_ADMIN");
		String jsonBody = objectMapper.writeValueAsString(roleDTO);

		ResultActions result = mockMvc
				.perform(put("/users/updateRole/{id}", existingId).header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(jsonBody));

		result.andExpect(status().isNoContent());
	}

	@Test
	public void updateRoleShouldReturnForbiddenWhenNotAdmin() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, password);

		RoleDTO roleDTO = new RoleDTO(1L, "ROLE_ADMIN");

		String jsonBody = objectMapper.writeValueAsString(roleDTO);

		ResultActions result = mockMvc
				.perform(put("/users/updateRole/{id}", existingId).header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(jsonBody));

		result.andExpect(status().isForbidden());
	}

	@Test
	public void updateRoleShouldReturnForbiddenWhenOperatorAuthenticated() throws Exception {
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, operatorUsername, password);

		RoleDTO roleDTO = new RoleDTO(1L, "ROLE_ADMIN");
		String jsonBody = objectMapper.writeValueAsString(roleDTO);

		ResultActions result = mockMvc
				.perform(put("/users/updateRole/{id}", existingId).header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON).content(jsonBody));

		result.andExpect(status().isForbidden());
	}

}