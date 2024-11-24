package com.superestoque.estoque.factories;

import com.superestoque.estoque.entities.Company;
import com.superestoque.estoque.entities.dto.CompanyDTO;

public class CompanyFactory {

	public static Company createCompany() {
		return new Company(50L, "Companhia das índias orientais",
				"123.456.87/9", null);
	}

	public static CompanyDTO createCompanyDTO() {
		Company company = createCompany();
		return new CompanyDTO(company);
	}
}