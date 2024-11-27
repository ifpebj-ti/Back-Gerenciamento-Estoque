package com.superestoque.estoque.services;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.superestoque.estoque.entities.Company;
import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.entities.dto.CompanyDTO;
import com.superestoque.estoque.factories.CompanyFactory;
import com.superestoque.estoque.factories.UserFactory;
import com.superestoque.estoque.repositories.CompanyRepository;
import com.superestoque.estoque.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTests {

	@InjectMocks
	private CompanyService service;

	@Mock
	private AuthService authService;

	@Mock
	private CompanyRepository repository;

	private Long existingCompanyId;
	private Long nonExistingCompanyId;
	private Company company;
	private User user;
	private MockMultipartFile validPhoto;
	private MockMultipartFile invalidPhoto;

	@BeforeEach
	void setUp() throws Exception {
		existingCompanyId = 1L;
		nonExistingCompanyId = 1000L;
		company = CompanyFactory.createCompany();
		user = UserFactory.createUser();
		validPhoto = new MockMultipartFile("photo", "test.jpg", "image/jpeg", "photo content".getBytes());
		invalidPhoto = new MockMultipartFile("photo", "test.txt", "text/plain", "invalid content".getBytes());

		Mockito.when(authService.authenticated()).thenReturn(user);
		Mockito.when(repository.findById(existingCompanyId)).thenReturn(Optional.of(company));
		Mockito.when(repository.findById(nonExistingCompanyId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(company);
	}

	@Test
	public void updateDataByUserShouldUpdateCompanyDataWhenValidPhoto() throws IOException {
		CompanyDTO result = service.updateDataByUser(existingCompanyId, "Updated Company", validPhoto);

		Assertions.assertNotNull(result);
		Assertions.assertEquals("Updated Company", result.getName());
		Mockito.verify(repository, Mockito.times(1)).findById(existingCompanyId);
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Company.class));
	}

	@Test
	public void updateDataByUserShouldThrowResourceNotFoundExceptionWhenCompanyDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.updateDataByUser(nonExistingCompanyId, "New Company", validPhoto);
		});

		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingCompanyId);
		Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(Company.class));
	}

	@Test
	public void updateDataByUserShouldThrowIllegalArgumentExceptionWhenInvalidPhoto() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			service.updateDataByUser(existingCompanyId, "Updated Company", invalidPhoto);
		});

		Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(Company.class));
	}

	@Test
	public void findAllUserByCompanyShouldThrowResourceNotFoundExceptionWhenCompanyDoesNotExist() {

		Assertions.assertThrows(java.lang.NullPointerException.class, () -> {
			service.findAllUserByCompany();
		});

	}
}