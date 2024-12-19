package com.superestoque.estoque.factories;

import java.math.BigDecimal;

import com.superestoque.estoque.entities.Company;
import com.superestoque.estoque.entities.Product;
import com.superestoque.estoque.entities.dto.ProductDTO;

public class ProductFactory {

	public static Product createProduct(Company company) {
		Product product = new Product(50L, "Produto 15", 10, "Descrição", null, 2, BigDecimal.TEN);
		product.setCompany(company);
		return product;
	}

	public static ProductDTO createProductDTO(Company company) {
		Product product = createProduct(company);
		product.setPhoto(new byte[1]);
		product.setCompany(company);
		return new ProductDTO(product);
	}
}