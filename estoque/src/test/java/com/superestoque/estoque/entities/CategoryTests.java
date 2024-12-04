package com.superestoque.estoque.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTests {

	@Test
	public void categoryShouldHaveCorrectStructure() {
		Category entity = new Category();

		entity.setName("Categoria teste");
		entity.setCompany(new Company(1L, "Companhia", "123456789", null));

		Assertions.assertNotNull(entity.getName());
		Assertions.assertEquals("Categoria teste", entity.getName());
		Assertions.assertNotNull(entity.getCompany());
		Assertions.assertEquals("Companhia", entity.getCompany().getName());

	}
}