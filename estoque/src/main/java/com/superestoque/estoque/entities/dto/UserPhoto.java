package com.superestoque.estoque.entities.dto;

import java.io.Serializable;

public class UserPhoto implements Serializable {

	private static final long serialVersionUID = 1L;

	private byte[] photo;

	public UserPhoto(byte[] photo) {
		this.photo = photo;
	}

	public byte[] getPhoto() {
		return photo;
	}

}