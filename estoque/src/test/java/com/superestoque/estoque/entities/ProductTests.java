package com.superestoque.estoque.entities;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductTests {

	@Test
	public void productShouldHaveCorrectStructure() {

		Product entity = new Product();

		entity.setName("Produto 01");
		entity.setQuantity(1);
		entity.setCritical_quantity(2);
		entity.setUnitValue(BigDecimal.valueOf(100.00));

		Assertions.assertNotNull(entity.getName());
		Assertions.assertEquals("Produto 01", entity.getName());
		Assertions.assertEquals(1, entity.getQuantity());
		Assertions.assertEquals(2, entity.getCritical_quantity());
		Assertions.assertNotNull(entity.getUnitValue());
		Assertions.assertEquals(BigDecimal.valueOf(100.00), entity.getUnitValue());
	}
}