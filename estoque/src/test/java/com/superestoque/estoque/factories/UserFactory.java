package com.superestoque.estoque.factories;

import java.util.UUID;

import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.entities.dto.UserDTO;

public class UserFactory {

	public static User createUser() {
		return new User(UUID.fromString("e3b9deaf-5e5f-424d-9063-cb32e1e7a6f3"), "Capitão América",
				"Capitão@vingadores.com", "FAZOL123");
	}

	public static UserDTO createUserDTO() {
		User user = createUser();
		return new UserDTO(user);
	}
}