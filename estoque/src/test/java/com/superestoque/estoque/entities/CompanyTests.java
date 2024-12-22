package com.superestoque.estoque.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompanyTests {

	@Test
	public void companyShouldHaveCorrectStructure() {
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
	public void equalsShouldReturnTrueWhenComparingSameObject() {
		Company company = new Company(1L, "Company A", "123456789", null);

		Assertions.assertTrue(company.equals(company));
	}

	@Test
	public void equalsShouldReturnFalseWhenComparingWithNull() {
		Company company = new Company(1L, "Company A", "123456789", null);

		Assertions.assertFalse(company.equals(null));
	}

	@Test
	public void equalsShouldReturnFalseWhenComparingWithDifferentClass() {
		Company company = new Company(1L, "Company A", "123456789", null);
		String differentClassObject = "Different Class";

		Assertions.assertFalse(company.equals(differentClassObject));
	}

	@Test
	public void equalsShouldReturnFalseWhenIdOrNameAreDifferent() {
		Company company1 = new Company(1L, "Company A", "123456789", null);
		Company company2 = new Company(2L, "Company A", "123456789", null);
		Company company3 = new Company(1L, "Company B", "123456789", null);

		Assertions.assertFalse(company1.equals(company2));
		Assertions.assertFalse(company1.equals(company3));
	}

	@Test
	public void equalsShouldReturnTrueWhenIdAndNameAreEqual() {
		Company company1 = new Company(1L, "Company A", "123456789", null);
		Company company2 = new Company(1L, "Company A", "987654321", null);

		Assertions.assertTrue(company1.equals(company2));
	}

	@Test
	public void hashCodeShouldBeConsistentWithEquals() {
		Company company1 = new Company(1L, "Company A", "123456789", null);
		Company company2 = new Company(1L, "Company A", "987654321", null);

		Assertions.assertEquals(company1.hashCode(), company2.hashCode());
	}

	@Test
	public void hashCodeShouldBeDifferentForDifferentObjects() {
		Company company1 = new Company(1L, "Company A", "123456789", null);
		Company company2 = new Company(2L, "Company B", "987654321", null);

		Assertions.assertNotEquals(company1.hashCode(), company2.hashCode());
	}
}