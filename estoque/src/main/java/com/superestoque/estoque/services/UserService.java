package com.superestoque.estoque.services;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.superestoque.estoque.entities.Role;
import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.entities.dto.RoleDTO;
import com.superestoque.estoque.entities.dto.UserDTO;
import com.superestoque.estoque.entities.dto.UserInsertDTO;
import com.superestoque.estoque.repositories.RoleRepository;
import com.superestoque.estoque.repositories.UserRepository;
import com.superestoque.estoque.services.exceptions.ResourceNotFoundException;


@Service
public class UserService implements UserDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;
	
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Autowired
	private AuthService authService;

	@Transactional
	public UserDTO findUserById() {
		User entity = authService.authenticated();
		LOG.info("Buscando usuário " + entity.getUsername());
		return new UserDTO(entity, entity.getRoles());
	}

	@Transactional
	public UserDTO saveUser(UserInsertDTO entity) {
		User user = new User();
		copyInsertDtoToEntity(user, entity);
		user.setStatus(true);
		repository.save(user);
		LOG.info("Criado usuário");
		return new UserDTO(user);
	}

	@Transactional
	public void desactivateUser(UUID id) {
		Optional<User> obj = repository.findById(id);
		User user = obj.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
		user.setStatus(false);
		LOG.info("Desativado o usuário " + id);
		repository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User entity = repository.findByEmail(username);
		if (entity == null) {
			throw new UsernameNotFoundException("Usuário e/ou senha inválidos.");
		}
		return entity;
	}

	private void copyInsertDtoToEntity(User user, UserInsertDTO dto) {
		user.setName(dto.getName());
		user.setPassword(encoder.encode(dto.getPassword()));
		user.setEmail(dto.getEmail());
		for (RoleDTO role : dto.getRoles()) {
			Role roleEntity = roleRepository.getReferenceById(role.getId());
			user.getRoles().add(roleEntity);
		}
	}
}