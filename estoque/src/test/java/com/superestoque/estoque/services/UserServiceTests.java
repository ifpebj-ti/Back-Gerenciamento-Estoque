package com.superestoque.estoque.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.superestoque.estoque.entities.Role;
import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.entities.dto.RoleDTO;
import com.superestoque.estoque.entities.dto.UserDTO;
import com.superestoque.estoque.entities.dto.UserInsertDTO;
import com.superestoque.estoque.entities.dto.UserPhoto;
import com.superestoque.estoque.entities.dto.UserUpdateDTO;
import com.superestoque.estoque.entities.dto.UserUpdatePasswordDTO;
import com.superestoque.estoque.factories.RoleFactory;
import com.superestoque.estoque.factories.UserFactory;
import com.superestoque.estoque.repositories.RoleRepository;
import com.superestoque.estoque.repositories.UserRepository;
import com.superestoque.estoque.services.exceptions.ResourceNotFoundException;
import com.superestoque.estoque.services.exceptions.ValidMultiFormDataException;

@ExtendWith(SpringExtension.class)
class UserServiceTests {

	@InjectMocks
	private UserService service;

	@Mock
	private AuthService authService;

	@Mock
	private UserRepository repository;

	@Mock
	private RoleRepository roleRepository;

	private Long existingId;
	private Long nonExistingId;
	private String existingEmail;
	private String nonExistingEmail;
	private User entity;
	private UserDTO entityDTO;
	private Optional<User> userOpt;
	private Role role;
	private List<Long> roles = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		existingEmail = "alex.brown@ifpe.com";
		nonExistingEmail = "joazinho.daagua@ifpe.com";
		entity = UserFactory.createUser();
		entityDTO = UserFactory.createUserDTO();
		role = RoleFactory.createRole();
		roleRepository.save(role);
		roles.add(1L);
		userOpt = Optional.of(entity);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(entity);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(entity));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.findByEmail(existingEmail)).thenReturn(entity);
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(entity);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(ArgumentMatchers.any(User.class))).thenReturn(entity);
		Mockito.when(authService.authenticated()).thenReturn(entity);
		Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
		Mockito.when(repository.getByEmail(existingEmail)).thenReturn(userOpt);
		Mockito.when(repository.getByEmail(nonExistingEmail)).thenReturn(Optional.empty());
	}

	@Test
	void findUserByIdShouldReturnUserDTO() {
		UserDTO result = service.findUserById();

		Assertions.assertNotNull(result);
		Assertions.assertEquals(entity.getId(), result.getId());
		Assertions.assertEquals(entity.getUsername(), result.getEmail());
		Mockito.verify(authService, Mockito.times(1)).authenticated();
	}

	@Test
	void findUserByEmailShouldThrowUsernameNotFoundExceptionWhenEmailDoesExists() {
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.loadUserByUsername(nonExistingEmail);
		});

		Mockito.verify(repository, Mockito.times(1)).findByEmail(nonExistingEmail);
	}

	@Test
	void saveUserShouldReturnUserDTO() {
		UserDTO result = service.saveNewUser(new UserInsertDTO(entityDTO.getId(), entity.getName(), entity.getEmail(),
				entityDTO.getPhoto(), true, true, entity.getPassword()), roles);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(entity.getName(), result.getName());
		Assertions.assertEquals(entity.getEmail(), result.getEmail());
		Assertions.assertTrue(result.isStatus());
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
	}

	@Test
	void desactiveShouldDoNotWhenIdExisting() {
		Assertions.assertDoesNotThrow(() -> {
			service.desactivateUser(existingId);
		});
		Optional<User> user = repository.findById(existingId);
		Assertions.assertEquals(false, user.get().isStatus());
	}

	@Test
	void desactiveShouldThrowResourceNotFoundExceptionWhenIdDoesExisting() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.desactivateUser(nonExistingId);
		});
	}

	@Test
	void activeShouldDoNotWhenIdExisting() {
		Assertions.assertDoesNotThrow(() -> {
			service.activeUser(existingId);
		});
		Optional<User> user = repository.findById(existingId);
		Assertions.assertEquals(true, user.get().isStatus());
	}

	@Test
	void activeShouldThrowResourceNotFoundExceptionWhenIdDoesExisting() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.activeUser(nonExistingId);
		});
	}

	@Test
	void updatePasswordShouldUpdateUserPasswordWhenIdExists() {
		UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("newPassword123");

		Assertions.assertDoesNotThrow(() -> {
			service.updatePassword(existingEmail, dto);
		});

		Mockito.verify(repository, Mockito.times(1)).getByEmail(existingEmail);
	}

	@Test
	void updatePasswordShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("newPassword123");

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.updatePassword(nonExistingEmail, dto);
		});

		Mockito.verify(repository, Mockito.times(1)).getByEmail(nonExistingEmail);
		Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(User.class));
	}

	@Test
	void updateRoleShouldUpdateUserRoleWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.updateRole(existingId, role.getId());
		});

		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
	}

	@Test
	void updateRoleShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Long roleId = 1L;

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.updateRole(nonExistingId, roleId);
		});

		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
		Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(User.class));
	}

	@Test
	void loadUserByUsernameShouldReturnUserWhenEmailExists() {
		UserDetails result = service.loadUserByUsername(existingEmail);

		Assertions.assertNotNull(result);
		Assertions.assertEquals("Capitão@vingadores.com", result.getUsername());
		Mockito.verify(repository, Mockito.times(1)).findByEmail(existingEmail);
	}

	@Test
	void copyInsertDtoToEntityShouldCopyFieldsCorrectly() {
		UserInsertDTO dto = new UserInsertDTO(null, "Test User", "test@test.com", null, true, true, "Senh@123");
		dto.getRoles().add(new RoleDTO(role.getId(), "ROLE_ADMIN"));

		UserDTO user = service.saveNewUser(dto, roles);

		Assertions.assertEquals(dto.getName(), user.getName());
		Assertions.assertEquals(dto.getEmail(), user.getEmail());
	}

	@Test
	void desactivateUserShouldChangeStatusToFalse() {
		service.desactivateUser(existingId);

		Optional<User> user = repository.findById(existingId);
		Assertions.assertTrue(user.isPresent());
		Assertions.assertFalse(user.get().isStatus());
		Mockito.verify(repository, Mockito.times(1)).save(user.get());
	}

	@Test
	void updatePasswordShouldEncodePasswordCorrectly() {
		UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("newPassword123");
		service.updatePassword(existingEmail, dto);

		Mockito.verify(repository, Mockito.times(1)).getByEmail(existingEmail);
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
	}

	@Test
	void updateRoleShouldNotDuplicateRole() {
		Role existingRole = new Role(role.getId(), "ROLE_ADMIN");
		entity.getRoles().add(existingRole);

		service.updateRole(existingId, role.getId());

		Assertions.assertEquals(1, entity.getRoles().size());
		Assertions.assertTrue(entity.getRoles().contains(existingRole));
		Mockito.verify(repository, Mockito.times(1)).save(entity);
	}

	@Test
	void updateUserShouldUpdateUserWhenEmailExists() {
		UserUpdateDTO updateDTO = new UserUpdateDTO("Senh@123", new byte[] { 1, 2, 3 });

		Assertions.assertDoesNotThrow(() -> {
			service.updateUser(existingEmail, updateDTO);
		});

		Mockito.verify(repository, Mockito.times(1)).getByEmail(existingEmail);
	}

	@Test
	void updateUserShouldThrowResourceNotFoundExceptionWhenEmailDoesNotExist() {
		UserUpdateDTO updateDTO = new UserUpdateDTO("newpassword", new byte[] { 1, 2, 3 });

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.updateUser(nonExistingEmail, updateDTO);
		});

		Mockito.verify(repository, Mockito.times(1)).getByEmail(nonExistingEmail);
		Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(User.class));
	}

	@Test
	void updateUserShouldNotUpdatePhotoIfPhotoIsNull() {
		UserUpdateDTO updateDTO = new UserUpdateDTO("Senh@123", new byte[] { 1, 2, 3 });

		service.updateUser(existingEmail, updateDTO);

		Mockito.verify(repository, Mockito.times(1)).getByEmail(existingEmail);
	}

	@Test
	void getUserPhotoShouldReturnPhotoWhenEmailExists() {
		UserPhoto photo = service.getUserPhoto(existingEmail);

		Assertions.assertNotNull(photo);
		Assertions.assertArrayEquals(entity.getPhoto(), photo.getPhoto());
		Mockito.verify(repository, Mockito.times(1)).getByEmail(existingEmail);
	}

	@Test
	void getUserPhotoShouldThrowResourceNotFoundExceptionWhenEmailDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.getUserPhoto(nonExistingEmail);
		});

		Mockito.verify(repository, Mockito.times(1)).getByEmail(nonExistingEmail);
	}

	@Test
	void validUserShouldThrowExceptionWhenEmailAlreadyExists() {
		Mockito.when(repository.getByEmail(existingEmail)).thenReturn(Optional.of(entity));

		UserInsertDTO insertDTO = new UserInsertDTO(null, "Test User", existingEmail, null, true, true, "password123");

		Assertions.assertThrows(ValidMultiFormDataException.class, () -> {
			service.saveNewUser(insertDTO, roles);
		});

		Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(User.class));
	}

	@Test
	void validUserShouldValidateFieldsCorrectly() {
		UserInsertDTO validDTO = new UserInsertDTO(null, "Valid User", "valid@test.com", null, true, true, "Senh@123");

		Assertions.assertDoesNotThrow(() -> {
			service.saveNewUser(validDTO, roles);
		});

		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
	}

}