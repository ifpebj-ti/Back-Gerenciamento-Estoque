package com.superestoque.estoque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.superestoque.estoque.entities.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
