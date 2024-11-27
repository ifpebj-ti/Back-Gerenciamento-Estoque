package com.superestoque.estoque.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.superestoque.estoque.entities.Company;
import com.superestoque.estoque.entities.Product;
import com.superestoque.estoque.factories.ProductFactory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private CompanyRepository companyRepository;

	private Long existingId;
	private Long nonExistingId;
	private Product product;
	private Company company;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		company = new Company(30L, "Empresa Teste", "12345678000100", null);
		company = companyRepository.save(company);
		product = ProductFactory.createProduct(company);
	}

	@Test
	public void saveShouldPersistProduct() {
		product = repository.save(product);

		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals("Produto 15", product.getName());
		Assertions.assertEquals(10, product.getQuantity());
	}

	@Test
	public void findByIdShouldReturnNonNullWhenIdExists() {
		Optional<Product> result = repository.findById(existingId);

		Assertions.assertTrue(result.isPresent());
	}

	@Test
	public void findByIdShouldReturnEmptyWhenIdNonExists() {
		Optional<Product> result = repository.findById(nonExistingId);

		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	public void deleteByIdShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Product> result = repository.findById(existingId);

		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void deleteByIdShouldDoNothingWhenIdNonExists() {
		repository.deleteById(nonExistingId);

		Optional<Product> result = repository.findById(nonExistingId);
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	public void saveShouldUpdateProductQuantity() {
		product = repository.getReferenceById(existingId);
		product.setQuantity(20);

		product = repository.save(product);

		Assertions.assertEquals(20, product.getQuantity());
	}

	@Test
	public void saveShouldCalculateStockValueCorrectly() {
		product.setQuantity(5);
		product.setUnitValue(new BigDecimal("100.00"));
		product.calculateStockValue();

		product = repository.save(product);

		Assertions.assertEquals(new BigDecimal("500.00"), product.getStockValue());
	}

	@Test
	public void saveShouldPersistProductWithCompany() {
		product.setCompany(company);

		product = repository.save(product);

		Assertions.assertNotNull(product.getCompany());
		Assertions.assertEquals("Empresa Teste", product.getCompany().getName());
	}

	@Test
	public void calculateStockValueShouldReturnTotalStockValue() {
		product.calculateStockValue();

		Assertions.assertEquals(BigDecimal.valueOf(100), product.getStockValue());
	}

}