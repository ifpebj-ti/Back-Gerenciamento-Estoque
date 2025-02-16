package com.superestoque.estoque.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.superestoque.estoque.entities.Category;
import com.superestoque.estoque.entities.Company;
import com.superestoque.estoque.entities.Product;
import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.entities.dto.CategoryDTO;
import com.superestoque.estoque.entities.dto.CompanyDTO;
import com.superestoque.estoque.entities.dto.ProductDTO;
import com.superestoque.estoque.repositories.ProductRepository;
import com.superestoque.estoque.services.exceptions.ResourceNotFoundException;
import com.superestoque.estoque.services.exceptions.ValidMultiFormDataException;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

	private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

	private final ProductRepository repository;

	private final CompanyService companyService;

	private final AuthService authService;

	private final CategoryService categoryService;

	private final EmailService emailService;

	public ProductService(ProductRepository repository, CompanyService companyService, AuthService authService,
			CategoryService categoryService, EmailService emailService) {
		this.repository = repository;
		this.companyService = companyService;
		this.authService = authService;
		this.categoryService = categoryService;
		this.emailService = emailService;
	}

	@Transactional
	public Page<ProductDTO> findAllProductByCompanyIdPaged(Pageable pageable, Long categoryId, String productName) {
		User user = authService.authenticated();
		Long companyId = user.getCompany().getId();

		Page<Product> products = repository.findByCompanyIdAndCategoryId(companyId, categoryId, productName, pageable);

	    LOG.info("Retornando página " + pageable.getPageNumber() +
	             " de produtos filtrados pela empresa " + companyId +
	             (categoryId != null ? " e pela categoria " + categoryId : "") +
	             (productName != null ? " e pelo nome " + productName : ""));

		return products.map(product -> new ProductDTO(product, product.getCategories()));
	}

	@Transactional
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product product = obj.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));
		LOG.info("Produto {} retornado com sucesso", product.getName());
		return new ProductDTO(product, product.getCategories());
	}

	@Transactional
	public void deleteById(Long id) {
		User user = authService.authenticated();
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Produto não encontrado");
		}
		repository.deleteById(id);
		LOG.info("Produto deletado com sucesso pelo usuário {}", user.getEmail());
	}

	@Transactional
	public ProductDTO saveNewProduct(ProductDTO entity, List<Long> categories) {
		Product product = new Product();
		copyInsertDtoToEntity(product, entity, categories);
		repository.save(product);
		LOG.info("Produto {} criado com sucesso.", product.getName());
		return new ProductDTO(product, product.getCategories());
	}

	@Transactional
	public ProductDTO updateProduct(Long id, ProductDTO entity, List<Long> categories) {
		Optional<Product> obj = repository.findById(id);
		Product product = obj.orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));
		updateData(product, entity, categories);
		product = repository.save(product);
		LOG.info("Atualizado dados do produto {} com sucesso.", id);
		return new ProductDTO(product);
	}

	private void copyInsertDtoToEntity(Product product, ProductDTO dto, List<Long> categories) {
		validProduct(dto);
		product.setName(dto.getName());
		product.setQuantity(dto.getQuantity());
		product.setDescription(dto.getDescription());
		product.setPhoto(dto.getPhoto());
		product.setCritical_quantity(dto.getCritical_quantity());
		CompanyDTO company = companyService.findById();
		product.setCompany(new Company(company));
		product.setUnitValue(dto.getUnitValue());
		product.calculateStockValue();
		if (categories.size() < 1) {
			throw new ValidMultiFormDataException("O produto deve conter ao menos uma categoria.");
		} else {
			for (Long id : categories) {
				CategoryDTO category = categoryService.findById(id);
				product.getCategories().add(new Category(category));
			}
		}
	}

	private void updateData(Product product, ProductDTO entity, List<Long> categories) {
		validProduct(entity);
		product.setName(entity.getName());
		if (entity.getPhoto() != null && entity.getPhoto().length > 0) {
			product.setPhoto(entity.getPhoto());
		}
		product.setQuantity(entity.getQuantity());
		product.setDescription(entity.getDescription());
		product.setUnitValue(entity.getUnitValue());
		product.setCritical_quantity(entity.getCritical_quantity());
		product.getCategories().clear();
		if (categories.isEmpty()) {
			throw new ValidMultiFormDataException("O produto deve conter ao menos uma categoria.");
		}
		for (Long id : categories) {
			CategoryDTO category = categoryService.findById(id);
			product.getCategories().add(new Category(category));
		}
		product.calculateStockValue();
		checkQuantity(product);
	}

	private void validProduct(ProductDTO product) {
		if (product.getName() == null || product.getName().isBlank()) {
			throw new ValidMultiFormDataException("O campo nome é obrigatório.");
		}

		if (product.getQuantity() < 1) {
			throw new ValidMultiFormDataException("O campo quantidade deve ser maior ou igual a um.");
		}

		if (product.getDescription().length() < 4) {
			throw new ValidMultiFormDataException("O campo descrição deve conter entre no mínimo 4 caracteres.");
		}

		if (product.getCritical_quantity() < 1) {
			throw new ValidMultiFormDataException("O campo quantidade crítica deve ser maior ou igual a um.");
		}
		if (product.getUnitValue() == null || product.getUnitValue().compareTo(BigDecimal.ZERO) < 1) {
			throw new ValidMultiFormDataException("O valor unitário deve ser maior ou igual a um.");
		}
		if (product.getStockValue() != null && product.getStockValue().compareTo(BigDecimal.ZERO) < 1) {
			throw new ValidMultiFormDataException("O valor do estoque deve ser maior ou igual a um.");
		}
	}

	private void checkQuantity(Product product) {
		if (product.getQuantity() <= product.getCritical_quantity()) {
			emailService.sendEmailProduct(product);
		}
	}
}