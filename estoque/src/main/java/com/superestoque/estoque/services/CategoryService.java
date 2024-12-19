package com.superestoque.estoque.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superestoque.estoque.entities.Category;
import com.superestoque.estoque.entities.Company;
import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.entities.dto.CategoryDTO;
import com.superestoque.estoque.entities.dto.CompanyDTO;
import com.superestoque.estoque.repositories.CategoryRepository;
import com.superestoque.estoque.services.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {

	private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

	@Autowired
	private CategoryRepository repository;

	@Autowired
	private AuthService authService;

	@Autowired
	private CompanyService companyService;

	@Transactional
	public List<CategoryDTO> findAllCategory() {
		User user = authService.authenticated();
		List<Category> objs = repository.findByCompanyId(user.getCompany().getId());
		List<CategoryDTO> entities = objs.stream().map(category -> new CategoryDTO(category))
				.collect(Collectors.toList());
		return entities;
	}
	
	@Transactional
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category category = obj.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));
		LOG.info("Categoria " + category.getName() + " retornado com sucesso!");
		return new CategoryDTO(category);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		CompanyDTO company = companyService.findById();
		entity.setCompany(new Company(company));
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Produto não encontrado");
		}
		User entity = authService.authenticated();
		repository.deleteById(id);
		LOG.info("Categoria deletada com sucesso pelo usuário " + entity.getName());
	}

}