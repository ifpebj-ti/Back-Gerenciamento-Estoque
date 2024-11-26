package com.superestoque.estoque.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.superestoque.estoque.controllers.exception.StandardError;
import com.superestoque.estoque.entities.dto.ProductDTO;
import com.superestoque.estoque.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

	@Autowired
	private ProductService service;

	@Operation(description = "Retrieves a paginated list of products by group", summary = "Fetches products with pagination by group", responses = {
			@ApiResponse(description = "Ok", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))) })
	@PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAllProductsPagedByGroup(@RequestParam(value = "categoryId", required = false) Long categoryId, Pageable pageable) {
		Page<ProductDTO> products = service.findAllProductByCompanyIdPaged(pageable, categoryId);
		return ResponseEntity.ok(products);
	}

	@Operation(description = "Fetches a product by ID", summary = "Retrieve a product by its ID", responses = {
			@ApiResponse(description = "Ok", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"timestamp\": \"2024-02-04T12:00:00Z\",\n\"status\": 404,\n\"error\": \"Recurso não encontrado\",\n\"message\": \"Produto não encontrado\",\n\"path\": \"/products/{id}\"}"), schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))) })
	@PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
		ProductDTO product = service.findById(id);
		return ResponseEntity.ok(product);
	}

	@Operation(description = "Deletes a product by ID", summary = "Delete a product using its ID", responses = {
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"timestamp\": \"2024-02-04T12:00:00Z\",\n\"status\": 404,\n\"error\": \"Recurso não encontrado\",\n\"message\": \"Produto não encontrado\",\n\"path\": \"/products/{id}\"}"), schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))) })
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(description = "Creates a new product with details including name, quantity, critical quantity, unit value, and photo.", summary = "Save a new product", responses = {
			@ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))) })
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<ProductDTO> saveNewProduct(@RequestParam String name, @RequestParam int quantity,
			@RequestParam("critical_quantity") int criticalQuantity, @RequestParam("unit_value") BigDecimal unitValue,
			@RequestParam MultipartFile photo) throws IOException {
		ProductDTO product = new ProductDTO(name, quantity, photo.getBytes(), criticalQuantity, unitValue);
		ProductDTO entity = service.saveNewProduct(product);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();
		return ResponseEntity.created(uri).body(entity);
	}

	@Operation(description = "Creates a new product with details including name, quantity, critical quantity, unit value, and photo.", summary = "Save a new product", responses = {
			@ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))) })
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/{id}", consumes = "multipart/form-data")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestParam String name,
			@RequestParam int quantity, @RequestParam("critical_quantity") int criticalQuantity,
			@RequestParam("unit_value") BigDecimal unitValue, @RequestParam MultipartFile photo) throws IOException {
		ProductDTO product = new ProductDTO(name, quantity, photo.getBytes(), criticalQuantity, unitValue);
		ProductDTO entity = service.updateProduct(id, product);
		return ResponseEntity.ok(entity);
	}

}