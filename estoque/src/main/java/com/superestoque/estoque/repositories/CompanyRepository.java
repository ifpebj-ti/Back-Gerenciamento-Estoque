package com.superestoque.estoque.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superestoque.estoque.entities.Company;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

}