package com.superestoque.estoque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superestoque.estoque.entities.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}