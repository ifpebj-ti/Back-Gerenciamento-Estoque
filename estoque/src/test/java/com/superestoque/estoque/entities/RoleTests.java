package com.superestoque.estoque.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.superestoque.estoque.entities.dto.RoleDTO;

class RoleTests {

	@Test
	void roleShouldHaveCorrectStructure() {

		Role entity = new Role();
		entity.setId(1L);
		entity.setAuthority("ROLE_MEMBER");

		Assertions.assertNotNull(entity.getClass());
		Assertions.assertNotNull(entity.getId());
		Assertions.assertEquals("ROLE_MEMBER", entity.getAuthority());
		Assertions.assertNotNull(entity.getAuthority());
		Assertions.assertEquals(1L, entity.getId());
	}

	@Test
	void roleConstructorShouldSetFieldsCorrectly() {
		RoleDTO dto = new RoleDTO(2L, "ROLE_ADMIN");
		Role entity = new Role(dto);

		Assertions.assertEquals(2L, entity.getId());
		Assertions.assertEquals("ROLE_ADMIN", entity.getAuthority());
	}

	@Test
	void equalsShouldReturnTrueWhenComparingSameObject() {
		Role role = new Role();
		role.setId(1L);
		role.setAuthority("ROLE_ADMIN");

		Assertions.assertEquals(role, role);
	}

	@Test
	void equalsShouldReturnFalseWhenComparingWithNull() {
		Role role = new Role();
		role.setId(1L);
		role.setAuthority("ROLE_ADMIN");

		Assertions.assertNotEquals(role, null);
	}

	@Test
	void equalsShouldReturnFalseWhenComparingDifferentClass() {
		Role role = new Role();
		role.setId(1L);
		role.setAuthority("ROLE_ADMIN");

		String differentClassObject = "Different Class Object";
		Assertions.assertNotEquals(role, differentClassObject);
	}

	@Test
	void equalsShouldReturnFalseWhenIdOrAuthorityAreDifferent() {
		Role role1 = new Role();
		role1.setId(1L);
		role1.setAuthority("ROLE_ADMIN");

		Role role2 = new Role();
		role2.setId(2L);
		role2.setAuthority("ROLE_ADMIN");

		Role role3 = new Role();
		role3.setId(1L);
		role3.setAuthority("ROLE_USER");

		Assertions.assertNotEquals(role1, role2);
		Assertions.assertNotEquals(role1, role3);
	}

	@Test
	void equalsShouldReturnTrueWhenIdAndAuthorityAreEqual() {
		Role role1 = new Role();
		role1.setId(1L);
		role1.setAuthority("ROLE_ADMIN");

		Role role2 = new Role();
		role2.setId(1L);
		role2.setAuthority("ROLE_ADMIN");

		Assertions.assertEquals(role1, role2);
	}
}