package com.superestoque.estoque.services;

import java.math.BigDecimal;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.superestoque.estoque.entities.Company;
import com.superestoque.estoque.entities.Product;
import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.entities.dto.CategoryDTO;
import com.superestoque.estoque.entities.dto.CompanyDTO;
import com.superestoque.estoque.entities.dto.ProductDTO;
import com.superestoque.estoque.factories.CategoryFactory;
import com.superestoque.estoque.factories.CompanyFactory;
import com.superestoque.estoque.factories.ProductFactory;
import com.superestoque.estoque.factories.UserFactory;
import com.superestoque.estoque.repositories.ProductRepository;
import com.superestoque.estoque.services.exceptions.ResourceNotFoundException;
import com.superestoque.estoque.services.exceptions.ValidMultiFormDataException;

@ExtendWith(SpringExtension.class)
class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@Mock
	private AuthService authService;

	@Mock
	private CompanyService companyService;

	@Mock
	private CategoryService categoryService;

	private Long existingId;
	private Long nonExistingId;
	private Long existingIdCategory;
	private Product product;
	private ProductDTO productDTO;
	private Company company;
	private CompanyDTO companyDTO;
	private User user;
	private List<Long> categories = new ArrayList<>();
	private CategoryDTO categoryDTO;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		existingIdCategory = 1L;
		company = CompanyFactory.createCompany();
		companyDTO = CompanyFactory.createCompanyDTO();
		product = ProductFactory.createProduct(company);
		productDTO = ProductFactory.createProductDTO(company);
		product.setCompany(company);
		user = UserFactory.createUser();
		user.setCompany(company);
		categories.add(1L);
		categoryDTO = CategoryFactory.createCompanyDTO();

		Mockito.when(authService.authenticated()).thenReturn(user);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		Mockito.when(
				repository.findByCompanyIdAndCategoryId(company.getId(), existingIdCategory, product.getName(), PageRequest.of(0, 10)))
				.thenReturn(new PageImpl<>(List.of(product)));
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(companyService.findById()).thenReturn(companyDTO);
		Mockito.when(categoryService.findById(ArgumentMatchers.anyLong())).thenReturn(categoryDTO);
	}

	@Test
	void findAllProductByCompanyIdPagedShouldReturnPagedProducts() {
		Page<ProductDTO> result = service.findAllProductByCompanyIdPaged(PageRequest.of(0, 10), existingIdCategory, product.getName());

		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(product.getName(), result.getContent().get(0).getName());
		Mockito.verify(repository, Mockito.times(1)).findByCompanyIdAndCategoryId(company.getId(), existingIdCategory,
				 product.getName(), PageRequest.of(0, 10));
	}

	@Test
	void findByIdShouldReturnProductWhenIdExists() {
		ProductDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(product.getName(), result.getName());
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}

	@Test
	void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});

		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
	}

	@Test
	void deleteByIdShouldDeleteProductWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.deleteById(existingId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}

	@Test
	void deleteByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.deleteById(nonExistingId);
		});

		Mockito.verify(repository, Mockito.times(1)).existsById(nonExistingId);
	}

	@Test
	void saveNewProductShouldSaveAndReturnProductDTO() {
		ProductDTO result = service.saveNewProduct(productDTO, categories);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(product.getName(), result.getName());
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Product.class));
	}

	@Test
	void updateProductShouldUpdateAndReturnProductDTOWhenIdExists() {
		ProductDTO updatedDTO = ProductFactory.createProductDTO(company);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		ProductDTO result = service.updateProduct(existingId, updatedDTO, categories);

		Assertions.assertNotNull(result);
		Assertions.assertEquals("Produto 15", result.getName());
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Product.class));
	}

	@Test
	void updateProductShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		ProductDTO updatedDTO = ProductFactory.createProductDTO(company);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.updateProduct(nonExistingId, updatedDTO, categories);
		});

		Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Product.class));
	}

	@Test
	void saveNewProductShouldThrowValidMultiFormDataExceptionWhenNameIsInvalid() {
		ProductDTO invalidDTO = new ProductDTO(null, "", 0, "", null, 0, null, null);

		Assertions.assertThrows(ValidMultiFormDataException.class, () -> {
			service.saveNewProduct(invalidDTO, categories);
		});

		Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(Product.class));
	}

	@Test
	void validProductShouldThrowExceptionWhenQuantityIsInvalid() {
		ProductDTO invalidDTO = ProductFactory.createProductDTO(company);
		invalidDTO.setQuantity(0);

		Assertions.assertThrows(ValidMultiFormDataException.class, () -> {
			service.saveNewProduct(invalidDTO, categories);
		});

		Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(Product.class));
	}

	@Test
	void validProductShouldThrowExceptionWhenUnitValueIsInvalid() {
		ProductDTO invalidDTO = ProductFactory.createProductDTO(company);
		invalidDTO.setUnitValue(BigDecimal.valueOf(0));

		Assertions.assertThrows(ValidMultiFormDataException.class, () -> {
			service.saveNewProduct(invalidDTO, categories);
		});

		Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(Product.class));
	}

	@Test
	void copyInsertDtoToEntityShouldThrowExceptionWhenCategoriesAreEmpty() {
		List<Long> emptyCategories = new ArrayList<>();

		Assertions.assertThrows(ValidMultiFormDataException.class, () -> {
			service.saveNewProduct(productDTO, emptyCategories);
		});

		Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(Product.class));
	}

	@Test
	void copyInsertDtoToEntityShouldSetFieldsCorrectly() {
		service.saveNewProduct(productDTO, categories);

		Mockito.verify(repository, Mockito.times(1))
				.save(Mockito.argThat(product -> product.getName().equals(productDTO.getName())
						&& product.getQuantity() == productDTO.getQuantity()
						&& product.getCategories().size() == categories.size()));
	}

	@Test
	void updateDataShouldHandleNullPhoto() {
		ProductDTO updatedDTO = ProductFactory.createProductDTO(company);
		updatedDTO.setPhoto(null);

		service.updateProduct(existingId, updatedDTO, categories);

		Mockito.verify(repository, Mockito.times(1)).save(Mockito.argThat(product -> product.getPhoto() == null));
	}
}