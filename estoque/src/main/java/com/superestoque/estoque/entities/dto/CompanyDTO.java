package com.superestoque.estoque.entities.dto;

import java.io.Serializable;
import java.util.UUID;

import com.superestoque.estoque.entities.Company;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CompanyDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	@Size(min = 4, max = 60, message = "O campo nome deve conter entre 4 e 60 caracteres")
	@NotBlank(message = "O campo nome é obrigatório")
	private String name;
	@Column(unique = true)
	private String cnpj;
	private byte[] photo;

	public CompanyDTO() {
	}

	public CompanyDTO(UUID id, String name, String cnpj, byte[] photo) {
		this.id = id;
		this.name = name;
		this.cnpj = cnpj;
		this.photo = photo;
	}

	public CompanyDTO(Company entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.cnpj = entity.getCnpj();
		this.photo = entity.getPhoto();
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

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

}