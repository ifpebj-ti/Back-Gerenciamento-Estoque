package com.superestoque.estoque.entities.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import com.superestoque.estoque.entities.Product;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	@Size(min = 4, max = 60, message = "O campo nome deve conter entre 4 e 60 caracteres")
	@NotBlank(message = "O campo nome é obrigatório")
	private String name;
	@Min(value = 0)
	private int quantity;
	@NotNull
	private byte[] photo;
	@Min(value = 0)
	private int critical_quantity;
	@Column(precision = 15, scale = 2)
	@Min(0)
	private BigDecimal unitValue;
	@Column(precision = 15, scale = 2)
	@Min(0)
	private BigDecimal stockValue;

	public ProductDTO() {
	}

	public ProductDTO(UUID id, String name, int quantity, byte[] photo, int critical_quantity, BigDecimal unitValue,
			BigDecimal stockValue) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.photo = photo;
		this.critical_quantity = critical_quantity;
		this.unitValue = unitValue;
		this.stockValue = stockValue;
	}

	public ProductDTO(String name, int quantity, byte[] photo, int critical_quantity, BigDecimal unitValue) {
		this.name = name;
		this.quantity = quantity;
		this.photo = photo;
		this.critical_quantity = critical_quantity;
		this.unitValue = unitValue;
	}

	public ProductDTO(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.quantity = entity.getQuantity();
		this.photo = entity.getPhoto();
		this.critical_quantity = entity.getCritical_quantity();
		this.unitValue = entity.getUnitValue();
		this.stockValue = entity.getStockValue();
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public int getCritical_quantity() {
		return critical_quantity;
	}

	public void setCritical_quantity(int critical_quantity) {
		this.critical_quantity = critical_quantity;
	}

	public BigDecimal getUnitValue() {
		return unitValue;
	}

	public void setUnitValue(BigDecimal unitValue) {
		this.unitValue = unitValue;
	}

	public BigDecimal getStockValue() {
		return stockValue;
	}

	public void setStockValue(BigDecimal stockValue) {
		this.stockValue = stockValue;
	}

}