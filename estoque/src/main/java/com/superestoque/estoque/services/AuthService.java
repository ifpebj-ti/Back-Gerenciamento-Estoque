package com.superestoque.estoque.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.repositories.UserRepository;
import com.superestoque.estoque.services.exceptions.UnauthorizedException;

@Service
public class AuthService {

	private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

	private final UserRepository userRepository;

	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User authenticated() {
		try {
			JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
					.getAuthentication();
			Jwt jwt = (Jwt) jwtToken.getPrincipal();
			String username = jwt.getClaimAsString("username");
			LOG.info("Retornando usuário logado com o email {} com sucesso.", username);
			return userRepository.findByEmail(username);
		} catch (Exception e) {
			throw new UnauthorizedException("Usuário inválido");
		}
	}

}