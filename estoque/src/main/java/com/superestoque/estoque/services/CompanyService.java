package com.superestoque.estoque.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.superestoque.estoque.entities.Company;
import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.entities.dto.CompanyDTO;
import com.superestoque.estoque.entities.dto.UserDTO;
import com.superestoque.estoque.repositories.CompanyRepository;
import com.superestoque.estoque.services.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class CompanyService {

	private static final Logger LOG = LoggerFactory.getLogger(CompanyService.class);

	private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList("image/jpeg", "image/png");

	@Autowired
	private CompanyRepository repository;

	@Autowired
	private AuthService authService;

	@Transactional
	public CompanyDTO findById() {
		User user = authService.authenticated();
		LOG.info("Usuário " + user.getUsername() + " buscando empresa com o id " + user.getCompany().getId());
		Company entity = repository.findById(user.getCompany().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada"));
		LOG.info("Empresa retornada com sucesso:" + entity.getId());
		return new CompanyDTO(entity);
	}

	@Transactional
	public CompanyDTO updateDataByUser(UUID id, String name, MultipartFile photo) throws IOException {
		LOG.info("Tentando atualizar nome e foto da empresa: " + id);
		if (!ALLOWED_FILE_TYPES.contains(photo.getContentType())) {
			throw new IllegalArgumentException("Tipo de arquivo inválido Somente JPEG e PNG são permitidos.");
		}
		Optional<Company> obj = repository.findById(id);
		Company company = obj.orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada."));
		updateData(company, name, photo);
		company = repository.save(company);
		LOG.info("Foto e nome da empresa" + id + " atualizada com sucesso!");
		return new CompanyDTO(company);
	}

	@Transactional
	public List<UserDTO> findAllUserByCompany() {
		User user = authService.authenticated();
		LOG.info("Usuário " + user.getUsername() + " buscando usuários da empresa " + user.getCompany().getId());
		Company entity = repository.findById(user.getCompany().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada"));
		List<UserDTO> entities = entity.getUsers().stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
		LOG.info("Usuários retornados com sucesso!");
		return entities;
	}

	private void updateData(Company company, String name, MultipartFile photo) {
		company.setName(name);
		LOG.info("Atualizado nome da empresa");
		try {
			company.setPhoto(photo.getBytes());
			LOG.info("Atualizado foto da empresa com sucesso!");
		} catch (IOException e) {
			throw new IllegalArgumentException("Erro ao atualizar a foto.");
		}
	}
}