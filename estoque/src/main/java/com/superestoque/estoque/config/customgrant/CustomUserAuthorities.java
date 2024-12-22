package com.superestoque.estoque.config.customgrant;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class CustomUserAuthorities {

	private String username;
	private Collection<? extends GrantedAuthority> authorities;
	private String name;
	private boolean firstAcess;

	public CustomUserAuthorities(String username, Collection<? extends GrantedAuthority> authorities, String name,
			boolean firstAcess) {
		this.username = username;
		this.authorities = authorities;
		this.name = name;
		this.firstAcess = firstAcess;
	}

	public String getUsername() {
		return username;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getName() {
		return name;
	}

	public boolean isFirstAcess() {
		return firstAcess;
	}

}