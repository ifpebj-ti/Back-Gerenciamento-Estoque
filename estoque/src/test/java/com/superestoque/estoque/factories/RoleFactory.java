package com.superestoque.estoque.factories;

import com.superestoque.estoque.entities.Role;
import com.superestoque.estoque.entities.dto.RoleDTO;

public class RoleFactory {

	public static Role createRole() {
		return new Role(4L, "ROLE_ADMIN");
	}

	public static RoleDTO createRoleDTO() {
		Role role = new Role(4L, "ROLE_ADMIN");
		return new RoleDTO(role);
	}

}