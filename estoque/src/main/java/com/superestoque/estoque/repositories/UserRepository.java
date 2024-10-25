package com.superestoque.estoque.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.superestoque.estoque.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	User findByEmail(String email);

	Optional<User> getByEmail(String email);
}