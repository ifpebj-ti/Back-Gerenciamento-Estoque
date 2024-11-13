package com.superestoque.estoque.entities;

import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserTests {

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Test
	public void userShouldHaveCorrectStructure() {

		User entity = new User();
		entity.setName("Name");
		entity.setEmail("email@gmail.com");
		entity.setPassword(encoder.encode(anyString()));
		entity.setStatus(true);
		entity.getRoles().add(new Role(1L, "ROLE_ADMIN"));

		Assertions.assertNotNull(entity.getClass());
		Assertions.assertNotNull(entity.getName());
		Assertions.assertEquals("Name", entity.getName());
		Assertions.assertNotNull(entity.getEmail());
		Assertions.assertNotNull(entity.getPassword());
		Assertions.assertEquals("email@gmail.com", entity.getEmail());
		Assertions.assertNotNull(entity.isStatus());
		Assertions.assertEquals(1, entity.getRoles().size());
	}
}