package com.superestoque.estoque.factories;

import java.math.BigDecimal;
import java.util.UUID;

import com.superestoque.estoque.entities.Company;
import com.superestoque.estoque.entities.Product;
import com.superestoque.estoque.entities.dto.ProductDTO;

public class ProductFactory {

	public static Product createProduct(Company company) {
		Product product = new Product(UUID.fromString("e3b9deaf-5e5f-424d-9063-cb32e1e7a6f5"), "Produto 15", 10, null,
				2, BigDecimal.TEN);
		product.setCompany(company);
		return product;
	}

	public static ProductDTO createProductDTO(Company company) {
		Product product = createProduct(company);
		return new ProductDTO(product);
	}
}