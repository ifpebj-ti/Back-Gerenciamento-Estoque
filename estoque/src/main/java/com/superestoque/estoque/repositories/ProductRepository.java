package com.superestoque.estoque.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.superestoque.estoque.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

	Page<Product> findByCompanyId(UUID id, Pageable pageable);
}