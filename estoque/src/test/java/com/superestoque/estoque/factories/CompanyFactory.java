package com.superestoque.estoque.factories;

import java.util.UUID;

import com.superestoque.estoque.entities.Company;
import com.superestoque.estoque.entities.dto.CompanyDTO;

public class CompanyFactory {

	public static Company createCompany() {
		return new Company(UUID.fromString("e3b9deaf-5e5f-424d-9063-cb32e1e7a6f5"), "Companhia das índias orientais",
				"123.456.87/9", null);
	}

	public static CompanyDTO createCompanyDTO() {
		Company company = createCompany();
		return new CompanyDTO(company);
	}
}