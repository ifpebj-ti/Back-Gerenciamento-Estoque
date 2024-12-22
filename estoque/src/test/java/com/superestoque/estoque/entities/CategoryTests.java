package com.superestoque.estoque.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTests {

	@Test
	public void categoryShouldHaveCorrectStructure() {
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
	public void equalsShouldReturnTrueWhenComparingSameObject() {
		Category category = new Category(1L, "Category A");

		Assertions.assertTrue(category.equals(category));
	}

	@Test
	public void equalsShouldReturnFalseWhenComparingWithNull() {
		Category category = new Category(1L, "Category A");

		Assertions.assertFalse(category.equals(null));
	}

	@Test
	public void equalsShouldReturnFalseWhenComparingWithDifferentClass() {
		Category category = new Category(1L, "Category A");
		String differentClassObject = "Different Class";

		Assertions.assertFalse(category.equals(differentClassObject));
	}

	@Test
	public void equalsShouldReturnFalseWhenIdIsDifferent() {
		Category category1 = new Category(1L, "Category A");
		Category category2 = new Category(2L, "Category A");

		Assertions.assertFalse(category1.equals(category2));
	}

	@Test
	public void equalsShouldReturnTrueWhenIdIsEqual() {
		Category category1 = new Category(1L, "Category A");
		Category category2 = new Category(1L, "Category B");

		Assertions.assertTrue(category1.equals(category2));
	}

	@Test
	public void equalsShouldReturnFalseWhenOneIdIsNull() {
		Category category1 = new Category(null, "Category A");
		Category category2 = new Category(1L, "Category A");

		Assertions.assertFalse(category1.equals(category2));
	}

	@Test
	public void equalsShouldReturnTrueWhenBothIdsAreNull() {
		Category category1 = new Category(null, "Category A");
		Category category2 = new Category(null, "Category B");

		Assertions.assertTrue(category1.equals(category2));
	}
}