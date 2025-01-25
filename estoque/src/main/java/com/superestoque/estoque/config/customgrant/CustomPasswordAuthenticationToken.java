package com.superestoque.estoque.config.customgrant;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

public class CustomPasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private final String username;
	private final String password;
	private final Set<String> scopes;

	public CustomPasswordAuthenticationToken(Authentication clientPrincipal, @Nullable Set<String> scopes,
			@Nullable Map<String, Object> additionalParameters) {

		super(new AuthorizationGrantType("password"), clientPrincipal, additionalParameters);

		this.username = additionalParameters != null ? (String) additionalParameters.get("username") : null;
		this.password = additionalParameters != null ? (String) additionalParameters.get("password") : null;
		this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public Set<String> getScopes() {
		return this.scopes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(password, scopes, username);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomPasswordAuthenticationToken other = (CustomPasswordAuthenticationToken) obj;
		return Objects.equals(password, other.password) && Objects.equals(scopes, other.scopes)
				&& Objects.equals(username, other.username);
	}

}