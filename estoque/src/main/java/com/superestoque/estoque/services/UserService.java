package com.superestoque.estoque.services;

import java.util.Optional;

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
import com.superestoque.estoque.entities.dto.UserUpdatePasswordDTO;
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
		LOG.info("Usuário " + entity.getUsername() + " retornado com sucesso!");
		return new UserDTO(entity, entity.getRoles());
	}

	@Transactional
	public UserDTO saveNewUser(UserInsertDTO entity) {
		User user = new User();
		copyInsertDtoToEntity(user, entity);
		user.setStatus(true);
		repository.save(user);
		LOG.info("Usuário "+ user.getUsername() + " criado com sucesso!");
		return new UserDTO(user);
	}

	@Transactional
	public void desactivateUser(Long id) {
		Optional<User> obj = repository.findById(id);
		User user = obj.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
		user.setStatus(false);
		LOG.info("Desativando o usuário " + id+ " com sucesso!");
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

	@Transactional
	public void updatePassword(Long id, UserUpdatePasswordDTO entity) {
		Optional<User> obj = repository.findById(id);
		User user = obj.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
		updateData(user, entity);
		user = repository.save(user);
		LOG.info("Atualizado senha do usuário " + id + " com sucesso!");
	}

	@Transactional
	public void updateRole(Long id, Long roleId) {
		Optional<User> obj = repository.findById(id);
		User user = obj.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
		updateUserRole(user, roleId);
		user = repository.save(user);
		LOG.info("Atualizado perfil do usuário " + id+ " para o perfil "+ roleId+ " com sucesso!");
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

	private void updateData(User user, UserUpdatePasswordDTO entity) {
		user.setPassword(encoder.encode(entity.getPassword()));
		LOG.info("Atualizado senha do usuário");
	}

	private void updateUserRole(User user, Long roleId) {
		Optional<Role> obj = roleRepository.findById(roleId);
		Role role = obj.orElseThrow(() -> new ResourceNotFoundException("Role não encontrada."));
		user.getRoles().clear();
		user.getRoles().add(role);
	}
}