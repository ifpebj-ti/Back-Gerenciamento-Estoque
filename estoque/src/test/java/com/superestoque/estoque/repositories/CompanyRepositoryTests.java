package com.superestoque.estoque.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.superestoque.estoque.entities.Company;
import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.factories.CompanyFactory;

@DataJpaTest
class CompanyRepositoryTests {

	@Autowired
	private CompanyRepository repository;

	private Long nonExistingId;
	private Long existingId;
	private Company company;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;

		nonExistingId = 1000L;
		company = CompanyFactory.createCompany();
	}

	@Test
	void saveShouldPersistCompany() {
		company = repository.save(company);

		Assertions.assertNotNull(company.getId());
		Assertions.assertEquals("Companhia das índias orientais", company.getName());
		Assertions.assertEquals("123.456.87/9", company.getCnpj());
	}

	@Test
	void findByIdShouldReturnNonNullWhenIdExists() {
		Optional<Company> result = repository.findById(existingId);

		Assertions.assertTrue(result.isPresent());
	}

	@Test
	void findByIdShouldReturnEmptyWhenIdNonExists() {
		Optional<Company> result = repository.findById(nonExistingId);

		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	void deleteByIdShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Company> result = repository.findById(existingId);

		Assertions.assertFalse(result.isPresent());
	}

	@Test
	void deleteByIdShouldDoNothingWhenIdNonExists() {
		repository.deleteById(nonExistingId);

		Optional<Company> result = repository.findById(nonExistingId);
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	void saveShouldPersistCompanyWithUsers() {
		User user = new User();
		user.setName("Test User");
		company.getUsers().add(user);

		company = repository.save(company);

		Assertions.assertEquals(1, company.getUsers().size());
	}

	@Test
	void saveShouldUpdateCompanyName() {
		company = repository.getReferenceById(existingId);
		company.setName("Empresa Atualizada");

		company = repository.save(company);

		Assertions.assertEquals("Empresa Atualizada", company.getName());
	}

}