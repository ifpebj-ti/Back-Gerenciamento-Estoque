package com.superestoque.estoque.entities;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductTests {

	@Test
	void productShouldHaveCorrectStructure() {

		Product entity = new Product();

		entity.setId(1L);
		entity.setName("Produto 01");
		entity.setQuantity(1);
		entity.setDescription("Descrição");
		entity.setCritical_quantity(2);
		entity.setUnitValue(BigDecimal.valueOf(100.00));

		Assertions.assertEquals(1, entity.getId());
		Assertions.assertNotNull(entity.getName());
		Assertions.assertEquals("Produto 01", entity.getName());
		Assertions.assertEquals(1, entity.getQuantity());
		Assertions.assertEquals("Descrição", entity.getDescription());
		Assertions.assertEquals(2, entity.getCritical_quantity());
		Assertions.assertNotNull(entity.getUnitValue());
		Assertions.assertEquals(BigDecimal.valueOf(100.00), entity.getUnitValue());
	}

	@Test
	void equalsShouldReturnTrueWhenComparingSameObject() {
		Product product = new Product(1L, "Product A", 10, "Description", null, 5, BigDecimal.valueOf(10.0));

		Assertions.assertEquals(product, product);
	}

	@Test
	void equalsShouldReturnFalseWhenComparingWithNull() {
		Product product = new Product(1L, "Product A", 10, "Description", null, 5, BigDecimal.valueOf(10.0));

		Assertions.assertNotEquals(product, null);
	}

	@Test
	void equalsShouldReturnFalseWhenComparingWithDifferentClass() {
		Product product = new Product(1L, "Product A", 10, "Description", null, 5, BigDecimal.valueOf(10.0));
		String differentClassObject = "Different Class";

		Assertions.assertNotEquals(product, differentClassObject);
	}

	@Test
	void equalsShouldReturnFalseWhenIdOrNameAreDifferent() {
		Product product1 = new Product(1L, "Product A", 10, "Description", null, 5, BigDecimal.valueOf(10.0));
		Product product2 = new Product(2L, "Product A", 10, "Description", null, 5, BigDecimal.valueOf(10.0));
		Product product3 = new Product(1L, "Product B", 10, "Description", null, 5, BigDecimal.valueOf(10.0));

		Assertions.assertNotEquals(product1, product2);
		Assertions.assertNotEquals(product1, product3);
	}

	@Test
	void equalsShouldReturnTrueWhenIdAndNameAreEqual() {
		Product product1 = new Product(1L, "Product A", 10, "Description", null, 5, BigDecimal.valueOf(10.0));
		Product product2 = new Product(1L, "Product A", 15, "Different Description", null, 5, BigDecimal.valueOf(15.0));

		Assertions.assertEquals(product1, product2);
	}
}