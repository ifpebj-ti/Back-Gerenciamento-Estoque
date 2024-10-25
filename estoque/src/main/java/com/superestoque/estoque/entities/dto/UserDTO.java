package com.superestoque.estoque.entities.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.superestoque.estoque.entities.Role;
import com.superestoque.estoque.entities.User;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	@Size(min = 4, max = 60, message = "O campo nome deve conter entre 4 e 60 caracteres")
	@NotBlank(message = "O campo nome é obrigatório")
	private String name;
	@Column(unique = true)
	@Email(message = "E-mail inválido")
	private String email;
	private boolean status;
	Set<RoleDTO> roles = new HashSet<>();;

	public UserDTO() {
	}

	public UserDTO(UUID id, String name, String email, boolean status) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.status = status;
	}

	public UserDTO(User entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.email = entity.getEmail();
		this.status = entity.isStatus();
	}

	public UserDTO(User entity, Set<Role> roles) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.email = entity.getEmail();
		this.status = entity.isStatus();
		entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

}