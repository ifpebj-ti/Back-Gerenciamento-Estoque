package com.superestoque.estoque.repositories;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.superestoque.estoque.entities.Role;
import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.factories.UserFactory;

@DataJpaTest
class UserRepositoryTests {

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	private Long existingId;
	private Long nonExistingId;
	private String nonExistingEmail;
	private User user;
	private Role role;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		nonExistingEmail = "thor@vingadores.com.br";
		user = UserFactory.createUser();
		role = new Role(1L, "ROLE_ADMIN");
	}

	@Test
	void deleteShouldReturnNulltWhenIdNonExists() {
		Optional<User> result = repository.findById(nonExistingId);

		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<User> result = repository.findById(existingId);

		Assertions.assertFalse(result.isPresent());
	}

	@Test
	void saveShouldPersistUser() {
		user.getRoles().add(role);
		user = repository.save(user);

		Assertions.assertNotNull(role.getId());
		Assertions.assertEquals(1, role.getId());
		Assertions.assertEquals(1, user.getRoles().size());
		Assertions.assertEquals(true, user.getRoles().contains(role));
		Assertions.assertEquals("Capitão América", user.getName());
	}

	@Test
	void saveShouldPersistUserWithTwoRoles() {
		user.getRoles().add(role);
		user.getRoles().add(new Role(2L, "ROLE_OPERATOR"));
		user = repository.save(user);

		Assertions.assertNotNull(role.getId());
		Assertions.assertEquals(1, role.getId());
		Assertions.assertEquals(2, user.getRoles().size());
		Assertions.assertEquals(true, user.getRoles().contains(role));
		Assertions.assertEquals("Capitão América", user.getName());
	}

	@Test
	void findByIdShouldReturnNullWhenIdNonExists() {
		Optional<User> result = repository.findById(nonExistingId);

		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	void findByIdShouldReturnNonNullWhenIdExists() {
		Optional<User> result = repository.findById(existingId);

		Assertions.assertTrue(result.isPresent());
	}

	@Test
	void findByEmailShouldReturnNullWhenEmailNonExists() {
		User user = repository.findByEmail(nonExistingEmail);

		Assertions.assertEquals(null, user);
	}

	@Test
	void findByEmailShouldReturnNonNullWhenEmailNonExists() {
		user = repository.save(user);
		User user = repository.findByEmail("alex.brown@ifpe.com");

		Assertions.assertEquals("Alex Brown", user.getName());
	}

	@Test
	void updateShouldPersistNewDatauserWhenIdExists() {
		user = repository.getReferenceById(existingId);
		user.setName("Nome atualiado");
		user.setEmail("email@atualizado.com");
		user = repository.save(user);

		Assertions.assertEquals("Nome atualiado", user.getName());
		Assertions.assertEquals("email@atualizado.com", user.getEmail());
	}

	@Test
	void updateShouldReturnNulltWhenIdNonExists() {
		Optional<User> result = repository.findById(nonExistingId);

		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	void updateRolesShouldPersistUpdatedRolesWhenIdExists() {
		user = repository.getReferenceById(existingId);
		Role newRole = new Role(2L, "ROLE_OPERATOR");
		user.getRoles().add(newRole);
		user = repository.save(user);

		Assertions.assertEquals(2, user.getRoles().size());
		Assertions.assertTrue(user.getRoles().contains(newRole));
	}

	@Test
	void deleteShouldRemoveUserButNotRolesWhenIdExists() {
		user.getRoles().add(role);
		user = repository.save(user);
		Long savedUserId = user.getId();
		repository.deleteById(savedUserId);
		Optional<User> deletedUser = repository.findById(savedUserId);

		Assertions.assertTrue(deletedUser.isEmpty());
		Assertions.assertNotNull(roleRepository.getReferenceById(role.getId()));
	}

	@Test
	void findAllShouldReturnAllUsers() {
		user = repository.save(user);
		List<User> users = repository.findAll();
		Assertions.assertFalse(users.isEmpty());
		Assertions.assertTrue(users.contains(user));
	}

}