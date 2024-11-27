package com.superestoque.estoque.services;

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
import com.superestoque.estoque.entities.dto.CompanyDTO;
import com.superestoque.estoque.entities.dto.ProductDTO;
import com.superestoque.estoque.factories.CompanyFactory;
import com.superestoque.estoque.factories.ProductFactory;
import com.superestoque.estoque.factories.UserFactory;
import com.superestoque.estoque.repositories.ProductRepository;
import com.superestoque.estoque.services.exceptions.ResourceNotFoundException;
import com.superestoque.estoque.services.exceptions.ValidMultiFormDataException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@Mock
	private AuthService authService;

	@Mock
	private CompanyService companyService;

	private Long existingId;
	private Long nonExistingId;
	private Product product;
	private ProductDTO productDTO;
	private Company company;
	private CompanyDTO companyDTO;
	private User user;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		company = CompanyFactory.createCompany();
		companyDTO = CompanyFactory.createCompanyDTO();
		product = ProductFactory.createProduct(company);
		productDTO = ProductFactory.createProductDTO(company);
		product.setCompany(company);
		user = UserFactory.createUser();
		user.setCompany(company);

		Mockito.when(authService.authenticated()).thenReturn(user);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		Mockito.when(repository.findByCompanyId(company.getId(), PageRequest.of(0, 10)))
				.thenReturn(new PageImpl<>(List.of(product)));
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(companyService.findById()).thenReturn(companyDTO);
	}

	@Test
	public void findAllProductByCompanyIdPagedShouldReturnPagedProducts() {
		Page<ProductDTO> result = service.findAllProductByCompanyIdPaged(PageRequest.of(0, 10));

		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(product.getName(), result.getContent().get(0).getName());
		Mockito.verify(repository, Mockito.times(1)).findByCompanyId(company.getId(), PageRequest.of(0, 10));
	}

	@Test
	public void findByIdShouldReturnProductWhenIdExists() {
		ProductDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(product.getName(), result.getName());
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});

		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
	}

	@Test
	public void deleteByIdShouldDeleteProductWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.deleteById(existingId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}

	@Test
	public void deleteByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.deleteById(nonExistingId);
		});

		Mockito.verify(repository, Mockito.times(1)).existsById(nonExistingId);
	}

	@Test
	public void saveNewProductShouldSaveAndReturnProductDTO() {
		ProductDTO result = service.saveNewProduct(productDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(product.getName(), result.getName());
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Product.class));
	}

	@Test
	public void updateProductShouldUpdateAndReturnProductDTOWhenIdExists() {
		ProductDTO updatedDTO = ProductFactory.createProductDTO(company);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		ProductDTO result = service.updateProduct(existingId, updatedDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals("Produto 15", result.getName());
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Product.class));
	}

	@Test
	public void updateProductShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		ProductDTO updatedDTO = ProductFactory.createProductDTO(company);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.updateProduct(nonExistingId, updatedDTO);
		});

		Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Product.class));
	}

	@Test
	public void saveNewProductShouldThrowValidMultiFormDataExceptionWhenNameIsInvalid() {
		ProductDTO invalidDTO = new ProductDTO(null, "", 0, null, 0, null, null);

		Assertions.assertThrows(ValidMultiFormDataException.class, () -> {
			service.saveNewProduct(invalidDTO);
		});

		Mockito.verify(repository, Mockito.never()).save(ArgumentMatchers.any(Product.class));
	}

}
