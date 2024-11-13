package com.superestoque.estoque.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.superestoque.estoque.entities.Role;
import com.superestoque.estoque.factories.RoleFactory;

@DataJpaTest
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository repository;

	private Long existingId;
	private Long nonExistingId;
	private Role role;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		role = RoleFactory.createRole();
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Role> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void saveShouldPersistRole() {
		role = repository.save(role);
		Assertions.assertNotNull(role.getId());
		Assertions.assertEquals(3, role.getId());
		Assertions.assertNotNull(role.getAuthority());
		Assertions.assertEquals("ROLE_ADMIN", role.getAuthority());
	}

	@Test
	public void findyByIdShouldReturnNulltWhenIdNonExists() {
		Optional<Role> result = repository.findById(nonExistingId);
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	public void findyByIdShouldReturnNonNulltWhenIdExists() {
		Optional<Role> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
}