package com.superestoque.estoque.entities.dto;

import java.io.Serializable;

public class UserUpdateDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public String password;
	private byte[] photo;

	public UserUpdateDTO() {
	}

	public UserUpdateDTO(String password, byte[] photo) {
		this.password = password;
		this.photo = photo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

}