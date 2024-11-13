package com.superestoque.estoque.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoleTests {

	@Test
	public void roleShouldHaveCorrectStructure() {

		Role entity = new Role();
		entity.setId(1L);
		entity.setAuthority("ROLE_MEMBER");

		Assertions.assertNotNull(entity.getClass());
		Assertions.assertNotNull(entity.getId());
		Assertions.assertEquals("ROLE_MEMBER", entity.getAuthority());
		Assertions.assertNotNull(entity.getAuthority());
		Assertions.assertEquals(1L, entity.getId());
	}
}