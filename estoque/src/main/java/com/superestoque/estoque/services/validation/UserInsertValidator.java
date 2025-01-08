package com.superestoque.estoque.services.validation;

import java.util.ArrayList;
import java.util.List;

import com.superestoque.estoque.controllers.exception.FieldMessage;
import com.superestoque.estoque.entities.User;
import com.superestoque.estoque.entities.dto.UserInsertDTO;
import com.superestoque.estoque.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

	private final UserRepository repository;

	public UserInsertValidator(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();
		User validEmail = repository.findByEmail(dto.getEmail());
		if (validEmail != null) {
			list.add(new FieldMessage("email", "Esse email já está cadastrado"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
