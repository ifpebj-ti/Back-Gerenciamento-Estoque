package com.superestoque.estoque.entities.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.superestoque.estoque.entities.Role;
import com.superestoque.estoque.entities.User;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	@Size(min = 4, max = 60, message = "O campo nome deve conter entre 4 e 60 caracteres")
	@NotBlank(message = "O campo nome é obrigatório")
	private String name;
	@Column(unique = true)
	@Email(message = "E-mail inválido")
	@NotBlank(message = "O campo nome é obrigatório")
	private String email;
	private byte[] photo;
	private boolean status;
	private boolean first_acess;
	Set<RoleDTO> roles = new HashSet<>();;

	public UserDTO() {
	}

	public UserDTO(Long id, String name, String email, byte[] photo, boolean status, boolean first_acess) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.photo = photo;
		this.status = status;
		this.first_acess = first_acess;
	}

	public UserDTO(User entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.email = entity.getEmail();
		this.photo = entity.getPhoto();
		this.status = entity.isStatus();
		this.first_acess = entity.isFirst_acess();
	}

	public UserDTO(User entity, Set<Role> roles) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.email = entity.getEmail();
		this.photo = entity.getPhoto();
		this.status = entity.isStatus();
		this.first_acess = entity.isFirst_acess();
		entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isFirst_acess() {
		return first_acess;
	}

	public void setFirst_acess(boolean first_acess) {
		this.first_acess = first_acess;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

}