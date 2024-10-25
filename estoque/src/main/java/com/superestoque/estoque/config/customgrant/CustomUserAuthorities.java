package com.superestoque.estoque.config.customgrant;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class CustomUserAuthorities {

	private String username;
	private Collection<? extends GrantedAuthority> authorities;
	private String name;

	public CustomUserAuthorities(String username, Collection<? extends GrantedAuthority> authorities, String name) {
		this.username = username;
		this.authorities = authorities;
		this.name = name;
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
	
	
}