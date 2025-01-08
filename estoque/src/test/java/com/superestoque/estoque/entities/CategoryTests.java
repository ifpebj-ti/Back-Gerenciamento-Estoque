package com.superestoque.estoque.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CategoryTests {

	@Test
	void categoryShouldHaveCorrectStructure() {
		Category entity = new Category();

		entity.setId(1L);
		entity.setName("Categoria teste");
		entity.setCompany(new Company(1L, "Companhia", "123456789", null));

		Assertions.assertEquals(1, entity.getId());
		Assertions.assertNotNull(entity.getName());
		Assertions.assertEquals("Categoria teste", entity.getName());
		Assertions.assertNotNull(entity.getCompany());
		Assertions.assertEquals("Companhia", entity.getCompany().getName());
	}

	@Test
	void equalsShouldReturnTrueWhenComparingSameObject() {
		Category category = new Category(1L, "Category A");

		Assertions.assertEquals(category, category);
	}

	@Test
	void equalsShouldReturnFalseWhenComparingWithNull() {
		Category category = new Category(1L, "Category A");

		Assertions.assertNotEquals(category, null);
	}

	@Test
	void equalsShouldReturnFalseWhenComparingWithDifferentClass() {
		Category category = new Category(1L, "Category A");
		String differentClassObject = "Different Class";

		Assertions.assertNotEquals(category, differentClassObject);
	}

	@Test
	void equalsShouldReturnFalseWhenIdIsDifferent() {
		Category category1 = new Category(1L, "Category A");
		Category category2 = new Category(2L, "Category A");

		Assertions.assertNotEquals(category1, category2);
	}

	@Test
	void equalsShouldReturnTrueWhenIdIsEqual() {
		Category category1 = new Category(1L, "Category A");
		Category category2 = new Category(1L, "Category B");

		Assertions.assertEquals(category1, category2);
	}

	@Test
	void equalsShouldReturnFalseWhenOneIdIsNull() {
		Category category1 = new Category(null, "Category A");
		Category category2 = new Category(1L, "Category A");

		Assertions.assertNotEquals(category1, category2);
	}

	@Test
	void equalsShouldReturnTrueWhenBothIdsAreNull() {
		Category category1 = new Category(null, "Category A");
		Category category2 = new Category(null, "Category B");

		Assertions.assertEquals(category1, category2);
	}
}