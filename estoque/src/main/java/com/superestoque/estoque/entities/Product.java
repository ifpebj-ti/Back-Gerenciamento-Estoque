package com.superestoque.estoque.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_product")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@UuidGenerator
	private UUID id;
	private String name;
	private int quantity;
	@Lob
	private byte[] photo;
	private int critical_quantity;
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	private BigDecimal unitValue;
	private BigDecimal stockValue;

	public Product() {
	}

	public Product(UUID id, String name, int quantity, byte[] photo, int critical_quantity, BigDecimal unitValue) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.photo = photo;
		this.critical_quantity = critical_quantity;
		this.unitValue = unitValue;
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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

	public void calculateStockValue() {
		this.stockValue = unitValue.multiply(new BigDecimal(quantity));
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
		Product other = (Product) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

}