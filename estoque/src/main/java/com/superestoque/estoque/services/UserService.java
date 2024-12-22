package com.superestoque.estoque.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.superestoque.estoque.entities.Role;
import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.entities.dto.UserDTO;
import com.superestoque.estoque.entities.dto.UserInsertDTO;
import com.superestoque.estoque.entities.dto.UserPhoto;
import com.superestoque.estoque.entities.dto.UserUpdateDTO;
import com.superestoque.estoque.entities.dto.UserUpdatePasswordDTO;
import com.superestoque.estoque.repositories.RoleRepository;
import com.superestoque.estoque.repositories.UserRepository;
import com.superestoque.estoque.services.exceptions.ResourceNotFoundException;
import com.superestoque.estoque.services.exceptions.ValidMultiFormDataException;

@Service
public class UserService implements UserDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	private AuthService authService;

	@Transactional
	public UserDTO findUserById() {
		User entity = authService.authenticated();
		LOG.info("Usuário " + entity.getUsername() + " retornado com sucesso!");
		return new UserDTO(entity, entity.getRoles());
	}

	@Transactional
	public UserDTO saveNewUser(UserInsertDTO entity, List<Long> roles) {
		User obj = authService.authenticated();
		User user = new User();
		copyInsertDtoToEntity(user, entity, roles);
		user.setCompany(obj.getCompany());
		user.setStatus(true);
		user.setFirst_acess(true);
		repository.save(user);
		LOG.info("Usuário " + user.getUsername() + " criado com sucesso!");
		return new UserDTO(user);
	}

	@Transactional
	public void desactivateUser(Long id) {
		Optional<User> obj = repository.findById(id);
		User user = obj.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
		user.setStatus(false);
		LOG.info("Desativando o usuário " + id + " com sucesso!");
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
	public void updatePassword(String email, UserUpdatePasswordDTO entity) {
		Optional<User> obj = repository.getByEmail(email);
		User user = obj.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
		updateData(user, entity);
		if (user.isFirst_acess()) {
			user.setFirst_acess(false);
		}
		user = repository.save(user);
		LOG.info("Atualizado senha do usuário " + email + " com sucesso!");
	}

	@Transactional
	public void updateRole(Long id, Long roleId) {
		Optional<User> obj = repository.findById(id);
		User user = obj.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
		updateUserRole(user, roleId);
		user = repository.save(user);
		LOG.info("Atualizado perfil do usuário " + id + " para o perfil " + roleId + " com sucesso!");
	}

	@Transactional
	public void updateUser(String email, UserUpdateDTO entity) {
		Optional<User> obj = repository.getByEmail(email);
		User user = obj.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
		validatePassword(entity.getPassword());
		user.setPassword(passwordEncoder.encode(entity.getPassword()));
		if (entity.getPhoto() != null && entity.getPhoto().length > 0) {
			user.setPhoto(entity.getPhoto());
		}
	}

	@Transactional
	public UserPhoto getUserPhoto(String email) {
		Optional<User> obj = repository.getByEmail(email);
		User user = obj.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
		UserPhoto entity = new UserPhoto(user.getPhoto());
		return entity;
	}

	private void copyInsertDtoToEntity(User user, UserInsertDTO dto, List<Long> roles) {
		validUser(dto);
		user.setName(dto.getName());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setEmail(dto.getEmail());
		user.setPhoto(dto.getPhoto());
		for (Long id : roles) {
			Role roleEntity = roleRepository.getReferenceById(id);
			user.getRoles().add(roleEntity);
		}
	}

	private void updateData(User user, UserUpdatePasswordDTO entity) {
		user.setPassword(passwordEncoder.encode(entity.getPassword()));
		LOG.info("Atualizado senha do usuário");
	}

	private void updateUserRole(User user, Long roleId) {
		Optional<Role> obj = roleRepository.findById(roleId);
		Role role = obj.orElseThrow(() -> new ResourceNotFoundException("Role não encontrada."));
		user.getRoles().clear();
		user.getRoles().add(role);
	}

	private void validUser(UserInsertDTO user) {
		validateField(user.getName(), "nome", 4, 60);
		validateField(user.getEmail(), "e-mail", 1, 255);
		validatePassword(user.getPassword());

		Optional<User> obj = repository.getByEmail(user.getEmail());

		if (obj.isPresent()) {
			throw new ValidMultiFormDataException("Email já cadastrado.");
		}
	}

	private void validateField(String field, String fieldName, int minLength, int maxLength) {
		if (field == null || field.isBlank()) {
			throw new ValidMultiFormDataException("O campo " + fieldName + " é obrigatório.");
		}
		if (field.length() < minLength || field.length() > maxLength) {
			throw new ValidMultiFormDataException(
					"O campo " + fieldName + " deve conter entre " + minLength + " e " + maxLength + " caracteres.");
		}
	}

	private void validatePassword(String password) {
		String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

		if (password.matches(passwordRegex)) {
			throw new ValidMultiFormDataException(
					"A senha deve conter no mínimo 8 caracteres, incluindo pelo menos 1 letra, 1 número e 1 caractere especial.");
		}
	}

}