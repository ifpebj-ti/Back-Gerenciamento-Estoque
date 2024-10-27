package com.superestoque.estoque.controllers;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.superestoque.estoque.controllers.exception.StandardError;
import com.superestoque.estoque.entities.dto.CompanyDTO;
import com.superestoque.estoque.entities.dto.UserDTO;
import com.superestoque.estoque.services.CompanyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	private CompanyService service;

	@Operation(description = "This endpoint is used for return a Company", summary = "Returns the currently logged in user company", responses = {
			@ApiResponse(description = "Ok", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyDTO.class))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"timestamp\": \"2024-02-04T12:00:00Z\",\n\"status\": 404,\n\"error\": \"Recurso não encontrado\",\n\"message\": \"Companhia não encontrada\",\n\"path\": \"/company/{id}\"}"), schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))) })
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<CompanyDTO> getMyCompany() {
		CompanyDTO entity = service.findById();
		return ResponseEntity.ok(entity);
	}

	@Operation(description = "This endpoint allows an ADMIN user to update the details of a specific company, including its name and photo.", summary = "Updates company details by ID", responses = {
			@ApiResponse(description = "Ok", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyDTO.class))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"timestamp\": \"2024-02-04T12:00:00Z\",\n\"status\": 404,\n\"error\": \"Recurso não encontrado\",\n\"message\": \"Companhia não encontrada\",\n\"path\": \"/company/{id}\"}"), schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))) })
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	public ResponseEntity<CompanyDTO> createCompany(@PathVariable UUID id, @RequestParam("name") String name,
			@RequestParam("photo") MultipartFile photo) throws IOException {
		CompanyDTO entity = service.updateDataByUser(id, name, photo);
		return ResponseEntity.ok(entity);
	}

	@Operation(description = "This endpoint is used to return a list of users by company", summary = "Returns a list of company users", responses = {
			@ApiResponse(description = "Ok", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = UserDTO.class))),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))) })
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/users")
	public ResponseEntity<List<UserDTO>> getUserMyCompany() {
		List<UserDTO> entities = service.findAllUserByCompany();
		return ResponseEntity.ok(entities);
	}

}