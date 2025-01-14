package com.superestoque.estoque.controllers;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.superestoque.estoque.controllers.exception.StandardError;
import com.superestoque.estoque.controllers.exception.ValidationError;
import com.superestoque.estoque.entities.dto.RoleDTO;
import com.superestoque.estoque.entities.dto.UserDTO;
import com.superestoque.estoque.entities.dto.UserInsertDTO;
import com.superestoque.estoque.entities.dto.UserPhoto;
import com.superestoque.estoque.entities.dto.UserUpdateDTO;
import com.superestoque.estoque.entities.dto.UserUpdatePasswordDTO;
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

	private final UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@Operation(description = "This endpoint is used for return a User", summary = "Returns the currently logged in user", responses = {
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
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<UserDTO> saveNewUser(@RequestParam String name, @RequestParam String email,
			@RequestParam String password, @RequestParam MultipartFile photo, @RequestParam List<Long> roles)
			throws IOException {
		UserInsertDTO user = new UserInsertDTO(null, name, email, photo.getBytes(), true, true, password);
		UserDTO entity = service.saveNewUser(user, roles);
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
	public ResponseEntity<Void> desactivateUser(@PathVariable Long id) {
		service.desactivateUser(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(description = "This endpoint is used for activate a user", summary = "Change status for a User", responses = {
			@ApiResponse(description = "No content", responseCode = "204"),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"timestamp\": \"2024-02-04T12:00:00Z\",\n\"status\": 404,\n\"error\": \"Recurso não encontrado\",\n\"message\": \"Usuário não encontrado\",\n\"path\": \"/users/{id}\"}"), schema = @Schema(implementation = StandardError.class))) })
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/active/{id}")
	public ResponseEntity<Void> activeUser(@PathVariable Long id) {
		service.activeUser(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(description = "This endpoint is used to update a user's password", summary = "Updates a user's password", responses = {
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Invalid request payload\"}"))) })
	@PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
	@PutMapping(value = "/updatePassword/{email}")
	public ResponseEntity<Void> updatePassword(@PathVariable String email,
			@Valid @RequestBody UserUpdatePasswordDTO entity) {
		service.updatePassword(email, entity);
		return ResponseEntity.noContent().build();
	}

	@Operation(description = "This endpoint is used to update a user's role", summary = "Updates a user's role", responses = {
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Invalid request payload\"}"))) })
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/updateRole/{id}")
	public ResponseEntity<Void> updateRole(@PathVariable Long id, @RequestBody RoleDTO role) {
		service.updateRole(id, role.getId());
		return ResponseEntity.noContent().build();
	}

	@Operation(description = "This endpoint is used to update a user's data", summary = "Updates a user's data", responses = {
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Invalid request payload\"}"))) })
	@PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
	@PutMapping(value = "/updateUser/{email}", consumes = "multipart/form-data")
	public ResponseEntity<Void> updateUser(@PathVariable String email, @RequestParam String password,
			@RequestParam MultipartFile photo) throws IOException {
		UserUpdateDTO entity = new UserUpdateDTO(password, photo.getBytes());
		service.updateUser(email, entity);
		return ResponseEntity.noContent().build();
	}

	@Operation(description = "This endpoint is used for return a User photo", summary = "Returns photo", responses = {
			@ApiResponse(description = "Ok", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserPhoto.class))),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"timestamp\": \"2024-02-04T12:00:00Z\",\n\"status\": 404,\n\"error\": \"Recurso não encontrado\",\n\"message\": \"Usuário não encontrado\",\n\"path\": \"/users/{email}\"}"), schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Unauthorized\"}"))),
			@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n\"message\": \"Forbidden\"}"))) })
	@PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
	@GetMapping(value = "/userPhoto/{email}")
	public ResponseEntity<UserPhoto> getPhoto(@PathVariable String email) {
		UserPhoto entity = service.getUserPhoto(email);
		return ResponseEntity.ok(entity);
	}

	@Operation(description = "This endpoint is used to request a password reset. An email with a reset link will be sent to the user.", summary = "Request password reset", responses = {
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
					    {
					        "timestamp": "2024-02-04T12:00:00Z",
					        "status": 404,
					        "error": "Recurso não encontrado",
					        "message": "Usuário não encontrado",
					        "path": "/users/reset-password"
					    }
					"""), schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
					    {
					        "message": "Invalid email format"
					    }
					"""))) })
	@PostMapping("/sendEmailResetPassword")
	public ResponseEntity<Void> requestPasswordReset(@RequestParam String email) {
		service.generatePasswordResetToken(email);
		return ResponseEntity.noContent().build();
	}

	@Operation(description = "This endpoint is used to reset a user's password. The user must provide a valid token and a new password.", summary = "Reset password with token", responses = {
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
					    {
					        "timestamp": "2024-02-04T12:00:00Z",
					        "status": 404,
					        "error": "Recurso não encontrado",
					        "message": "Token inválido ou não encontrado",
					        "path": "/users/reset-password"
					    }
					"""), schema = @Schema(implementation = StandardError.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
					    {
					        "message": "Token expirado"
					    }
					"""))) })
	@PutMapping("/reset-password")
	public ResponseEntity<Void> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
		service.updatePasswordWithToken(token, newPassword);
		return ResponseEntity.noContent().build();
	}

}