package com.superestoque.estoque.services;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.superestoque.estoque.entities.Role;
import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.entities.dto.UserDTO;
import com.superestoque.estoque.entities.dto.UserInsertDTO;
import com.superestoque.estoque.entities.dto.UserUpdatePasswordDTO;
import com.superestoque.estoque.factories.RoleFactory;
import com.superestoque.estoque.factories.UserFactory;
import com.superestoque.estoque.repositories.RoleRepository;
import com.superestoque.estoque.repositories.UserRepository;
import com.superestoque.estoque.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

	@InjectMocks
	private UserService service;

	@Mock
	private AuthService authService;

	@Mock
	private UserRepository repository;

	@Mock
	private RoleRepository roleRepository;

	private UUID existingId;
	private UUID nonExistingId;
	private String existingEmail;
	private String nonExistingEmail;
	private User entity;
	private UserDTO entityDTO;
	private Role role;

	@BeforeEach
	void setUp() throws Exception {
		existingId = UUID.fromString("e3b9deaf-5e5f-424d-9063-cb32e1e7a6f4");
		nonExistingId = UUID.randomUUID();
		existingEmail = "alex.brown123@ifpe.com";
		nonExistingEmail = "joazinho.daagua@ifpe.com";
		entity = UserFactory.createUser();
		entityDTO = UserFactory.createUserDTO();
		role = RoleFactory.createRole();
		roleRepository.save(role);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(entity);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(entity));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.findByEmail(existingEmail)).thenReturn(entity);
		Mockito.when(repository.findByEmail(existingEmail)).thenReturn(null);
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(entity);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(entity);
		Mockito.when(authService.authenticated()).thenReturn(entity);
		Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
	}

	@Test
	public void findUserByIdShouldReturnUserDTO() {
		UserDTO result = service.findUserById();

		Assertions.assertNotNull(result);
		Assertions.assertEquals(entity.getId(), result.getId());
		Assertions.assertEquals(entity.getUsername(), result.getEmail());
		Mockito.verify(authService, Mockito.times(1)).authenticated();
	}

	@Test
	public void findUserByEmailShouldThrowUsernameNotFoundExceptionWhenEmailDoesExists() {
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.loadUserByUsername(nonExistingEmail);
		});

		Mockito.verify(repository, Mockito.times(1)).findByEmail(nonExistingEmail);
	}

	@Test
	public void saveUserShouldReturnUserDTO() {
		UserDTO result = service.saveNewUser(
				new UserInsertDTO(entityDTO.getId(), entity.getName(), entity.getEmail(), true, entity.getPassword()));

		Assertions.assertNotNull(result);
		Assertions.assertEquals(entity.getName(), result.getName());
		Assertions.assertEquals(entity.getEmail(), result.getEmail());
		Assertions.assertTrue(result.isStatus());
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
	}

	@Test
	public void desactiveShouldDoNotWhenIdExisting() {
		Assertions.assertDoesNotThrow(() -> {
			service.desactivateUser(existingId);
		});
		Optional<User> user = repository.findById(existingId);
		Assertions.assertEquals(false, user.get().isStatus());
	}

	@Test
	public void desactiveShouldThrowResourceNotFoundExceptionWhenIdDoesExisting() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.desactivateUser(nonExistingId);
		});
	}

	@Test
	public void updatePasswordShouldUpdateUserPasswordWhenIdExists() {
		UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("newPassword123");

		Assertions.assertDoesNotThrow(() -> {
			service.updatePassword(existingId, dto);
		});

		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}

	@Test
	public void updatePasswordShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("newPassword123");

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.updatePassword(nonExistingId, dto);
		});

		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
		Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(User.class));
	}

	@Test
	public void updateRoleShouldUpdateUserRoleWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.updateRole(existingId, role.getId());
		});

		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
	}

	@Test
	public void updateRoleShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Long roleId = 1L;

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.updateRole(nonExistingId, roleId);
		});

		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
		Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(User.class));
	}
}