package com.superestoque.estoque.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.repositories.UserRepository;
import com.superestoque.estoque.services.exceptions.ForbiddenException;
import com.superestoque.estoque.services.exceptions.UnauthorizedException;

@Service
public class AuthService {

	private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private UserRepository userRepository;


	@Transactional
	public User authenticated() {
		try {
			JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) SecurityContextHolder.getContext()
					.getAuthentication();
			Jwt jwt = (Jwt) jwtToken.getPrincipal();
			String username = jwt.getClaimAsString("username");
			LOG.info("Retornando usuário logado com o email " + username + " com sucesso!");
			return userRepository.findByEmail(username);
		} catch (Exception e) {
			throw new UnauthorizedException("Usuário inválido");
		}
	}

	public void validateSelfOrAdmin(UUID userId) {
		User user = authenticated();
		if (!user.hasRole("ROLE_ADMIN")) {
			throw new ForbiddenException("Acesso negado");
		}
	}

}