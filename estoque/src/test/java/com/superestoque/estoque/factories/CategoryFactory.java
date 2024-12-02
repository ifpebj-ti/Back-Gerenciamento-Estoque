package com.superestoque.estoque.factories;

import com.superestoque.estoque.entities.Category;
import com.superestoque.estoque.entities.dto.CategoryDTO;

public class CategoryFactory {

	public static Category createCategory() {
		return new Category(50L, "Categoria");
	}

	public static CategoryDTO createCompanyDTO() {
		Category category = createCategory();
		return new CategoryDTO(category);
	}
}