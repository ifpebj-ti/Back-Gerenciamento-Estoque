package com.superestoque.estoque.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.superestoque.estoque.entities.dto.CompanyDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_company")
public class Company implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String cnpj;
	@Lob
	private byte[] photo;
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<User> users = new HashSet<>();
	@OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
	private Set<Product> products = new HashSet<>();

	public Company() {
	}

	public Company(Long id, String name, String cnpj, byte[] photo) {
		this.id = id;
		this.name = name;
		this.cnpj = cnpj;
		this.photo = photo;
	}

	public Company(CompanyDTO entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.cnpj = entity.getCnpj();
		this.photo = entity.getPhoto();
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

	public Set<User> getUsers() {
		return users;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

}