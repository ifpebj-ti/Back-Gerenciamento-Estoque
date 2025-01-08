package com.superestoque.estoque.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CompanyTests {

	@Test
	void companyShouldHaveCorrectStructure() {
		Company entity = new Company();

		entity.setId(1L);
		entity.setCnpj("123456-9");
		entity.setName("Companhia das indias orientais");

		Assertions.assertEquals(1, entity.getId());
		Assertions.assertNotNull(entity.getCnpj());
		Assertions.assertEquals("123456-9", entity.getCnpj());
		Assertions.assertNotNull(entity.getName());
		Assertions.assertEquals("Companhia das indias orientais", entity.getName());
	}

	@Test
	void equalsShouldReturnTrueWhenComparingSameObject() {
		Company company = new Company(1L, "Company A", "123456789", null);

		Assertions.assertEquals(company, company);
	}

	@Test
	void equalsShouldReturnFalseWhenComparingWithNull() {
		Company company = new Company(1L, "Company A", "123456789", null);

		Assertions.assertNotEquals(company, null);
	}

	@Test
	void equalsShouldReturnFalseWhenComparingWithDifferentClass() {
		Company company = new Company(1L, "Company A", "123456789", null);
		String differentClassObject = "Different Class";

		Assertions.assertNotEquals(company, differentClassObject);
	}

	@Test
	void equalsShouldReturnFalseWhenIdOrNameAreDifferent() {
		Company company1 = new Company(1L, "Company A", "123456789", null);
		Company company2 = new Company(2L, "Company A", "123456789", null);
		Company company3 = new Company(1L, "Company B", "123456789", null);

		Assertions.assertNotEquals(company1, company2);
		Assertions.assertNotEquals(company1, company3);
	}

	@Test
	void equalsShouldReturnTrueWhenIdAndNameAreEqual() {
		Company company1 = new Company(1L, "Company A", "123456789", null);
		Company company2 = new Company(1L, "Company A", "987654321", null);

		Assertions.assertEquals(company1, company2);
	}

	@Test
	void hashCodeShouldBeConsistentWithEquals() {
		Company company1 = new Company(1L, "Company A", "123456789", null);
		Company company2 = new Company(1L, "Company A", "987654321", null);

		Assertions.assertEquals(company1.hashCode(), company2.hashCode());
	}

	@Test
	void hashCodeShouldBeDifferentForDifferentObjects() {
		Company company1 = new Company(1L, "Company A", "123456789", null);
		Company company2 = new Company(2L, "Company B", "987654321", null);

		Assertions.assertNotEquals(company1.hashCode(), company2.hashCode());
	}
}