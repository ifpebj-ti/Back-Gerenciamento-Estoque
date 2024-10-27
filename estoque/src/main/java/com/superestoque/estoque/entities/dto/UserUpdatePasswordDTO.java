package com.superestoque.estoque.entities.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserUpdatePasswordDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Size(min = 8, message = "A senha deve conter no mínimo oito caracteres")
	@NotBlank(message = "O campo nome é obrigatório")
	private String password;

	public UserUpdatePasswordDTO() {
	}

	public UserUpdatePasswordDTO(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}