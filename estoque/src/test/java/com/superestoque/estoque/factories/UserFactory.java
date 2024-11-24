package com.superestoque.estoque.factories;

import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.entities.dto.UserDTO;

public class UserFactory {

	public static User createUser() {
		return new User(50L, "Capitão América", "Capitão@vingadores.com", "FAZOL123");
	}

	public static UserDTO createUserDTO() {
		User user = createUser();
		return new UserDTO(user);
	}
}