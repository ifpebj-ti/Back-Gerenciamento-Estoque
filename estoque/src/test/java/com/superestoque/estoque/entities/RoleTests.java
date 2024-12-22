package com.superestoque.estoque.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.superestoque.estoque.entities.dto.RoleDTO;

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

	@Test
	public void roleConstructorShouldSetFieldsCorrectly() {
		RoleDTO dto = new RoleDTO(2L, "ROLE_ADMIN");
		Role entity = new Role(dto);

		Assertions.assertEquals(2L, entity.getId());
		Assertions.assertEquals("ROLE_ADMIN", entity.getAuthority());
	}

	@Test
	public void equalsShouldReturnTrueWhenComparingSameObject() {
		Role role = new Role();
		role.setId(1L);
		role.setAuthority("ROLE_ADMIN");

		Assertions.assertTrue(role.equals(role));
	}

	@Test
	public void equalsShouldReturnFalseWhenComparingWithNull() {
		Role role = new Role();
		role.setId(1L);
		role.setAuthority("ROLE_ADMIN");

		Assertions.assertFalse(role.equals(null));
	}

	@Test
	public void equalsShouldReturnFalseWhenComparingDifferentClass() {
		Role role = new Role();
		role.setId(1L);
		role.setAuthority("ROLE_ADMIN");

		String differentClassObject = "Different Class Object";
		Assertions.assertFalse(role.equals(differentClassObject));
	}

	@Test
	public void equalsShouldReturnFalseWhenIdOrAuthorityAreDifferent() {
		Role role1 = new Role();
		role1.setId(1L);
		role1.setAuthority("ROLE_ADMIN");

		Role role2 = new Role();
		role2.setId(2L);
		role2.setAuthority("ROLE_ADMIN");

		Role role3 = new Role();
		role3.setId(1L);
		role3.setAuthority("ROLE_USER");

		Assertions.assertFalse(role1.equals(role2));
		Assertions.assertFalse(role1.equals(role3));
	}

	@Test
	public void equalsShouldReturnTrueWhenIdAndAuthorityAreEqual() {
		Role role1 = new Role();
		role1.setId(1L);
		role1.setAuthority("ROLE_ADMIN");

		Role role2 = new Role();
		role2.setId(1L);
		role2.setAuthority("ROLE_ADMIN");

		Assertions.assertTrue(role1.equals(role2));
	}
}