package com.superestoque.estoque.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.superestoque.estoque.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT DISTINCT p FROM Product p " + "LEFT JOIN FETCH p.categories c " + "WHERE p.company.id = :companyId "
			+ "AND (:categoryId IS NULL OR c.id = :categoryId)")
	Page<Product> findByCompanyIdAndCategoryId(@Param("companyId") Long companyId, @Param("categoryId") Long categoryId,
			Pageable pageable);

}