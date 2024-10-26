package com.superestoque.estoque.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.superestoque.estoque.controllers.exception.StandardError;
import com.superestoque.estoque.controllers.exception.ValidationError;
import com.superestoque.estoque.entities.dto.UserDTO;
import com.superestoque.estoque.entities.dto.UserInsertDTO;
import com.superestoque.estoque.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService service;

	@Operation(description = "This endpoint is used for return a User", summary = "Returns the currently logged in user user", responses = {
			@ApiResponse(description = "Ok", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"timestamp\": \"2024-02-04T12:00:00Z\",\n\"status\": 404,\n\"error\": \"Recurso não encontrado\",\n\"message\": \"Usuário não encontrado\",\n\"path\": \"/users/{id}\"}"), schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))) })
	@PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
	@GetMapping(value = "/me")
	public ResponseEntity<UserDTO> findUserById() {
		UserDTO entity = service.findUserById();
		return ResponseEntity.ok(entity);
	}

	@Operation(description = "This endpoint is used for save a User when data is valid", summary = "Create a new User", responses = {
			@ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationError.class))) })
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<UserDTO> save(@Valid @RequestBody UserInsertDTO dto) {
		UserDTO entity = service.saveUser(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();
		return ResponseEntity.created(uri).body(entity);
	}
	
	@Operation(description = "This endpoint is used for desactivate a user", summary = "Change status for a User", responses = {
			@ApiResponse(description = "No content", responseCode = "204"),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"timestamp\": \"2024-02-04T12:00:00Z\",\n\"status\": 404,\n\"error\": \"Recurso não encontrado\",\n\"message\": \"Usuário não encontrado\",\n\"path\": \"/users/{id}\"}"), schema = @Schema(implementation = StandardError.class))) })
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/desactive/{id}")
	public ResponseEntity<Void> desactivateUser(@PathVariable UUID id) {
		service.desactivateUser(id);
		return ResponseEntity.noContent().build();
	}

}
