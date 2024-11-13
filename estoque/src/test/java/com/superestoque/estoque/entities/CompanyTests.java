package com.superestoque.estoque.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompanyTests {

	@Test
	public void companShouldHaveCorrectStructure() {
		Company entity = new Company();

		entity.setCnpj("123456-9");
		entity.setName("Companhia das indias orientais");

		Assertions.assertNotNull(entity.getCnpj());
		Assertions.assertEquals("123456-9", entity.getCnpj());
		Assertions.assertNotNull(entity.getName());
		Assertions.assertEquals("Companhia das indias orientais", entity.getName());

	}
}