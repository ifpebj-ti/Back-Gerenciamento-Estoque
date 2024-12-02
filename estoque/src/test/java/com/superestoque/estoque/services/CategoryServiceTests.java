package com.superestoque.estoque.services;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.superestoque.estoque.entities.Category;
import com.superestoque.estoque.entities.Company;
import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.entities.dto.CategoryDTO;
import com.superestoque.estoque.entities.dto.CompanyDTO;
import com.superestoque.estoque.factories.CategoryFactory;
import com.superestoque.estoque.factories.CompanyFactory;
import com.superestoque.estoque.factories.UserFactory;
import com.superestoque.estoque.repositories.CategoryRepository;
import com.superestoque.estoque.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

	@InjectMocks
	private CategoryService service;

	@Mock
	private AuthService authService;

	@Mock
	private CompanyService companyService;

	@Mock
	private CategoryRepository repository;

	private Company company;
	private User user;
	private Category category;
	private CategoryDTO categoryDTO;

	@BeforeEach
	void setUp() throws Exception {
		user = UserFactory.createUser();
		company = CompanyFactory.createCompany();
		category = CategoryFactory.createCategory();
		categoryDTO = CategoryFactory.createCompanyDTO();
		user.setCompany(company);

		Mockito.when(authService.authenticated()).thenReturn(user);
		Mockito.when(companyService.findById()).thenReturn(new CompanyDTO(company));
		Mockito.when(repository.findByCompanyId(company.getId())).thenReturn(List.of(category));
		Mockito.when(repository.save(Mockito.any())).thenReturn(category);
		Mockito.when(repository.existsById(category.getId())).thenReturn(true);
	}

	@Test
	public void findAllCategoryShouldReturnListOfCategoriesWhenUserIsAuthenticated() {
		List<CategoryDTO> result = service.findAllCategory();

		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(category.getName(), result.get(0).getName());
		Mockito.verify(repository, Mockito.times(1)).findByCompanyId(user.getCompany().getId());
	}

	@Test
	public void deleteShouldRemoveCategoryWhenIdExists() {
		service.delete(category.getId());

		Mockito.verify(repository, Mockito.times(1)).existsById(category.getId());
		Mockito.verify(repository, Mockito.times(1)).deleteById(category.getId());
	}

	@Test
	public void insertShouldPersistCategoryAndReturnCategoryDTO() {
		CategoryDTO result = service.insert(categoryDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(category.getName(), result.getName());
		Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Category.class));
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Mockito.when(repository.existsById(category.getId())).thenReturn(false);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(category.getId());
		});

		Mockito.verify(repository, Mockito.times(1)).existsById(category.getId());
		Mockito.verify(repository, Mockito.never()).deleteById(Mockito.anyLong());
	}
}