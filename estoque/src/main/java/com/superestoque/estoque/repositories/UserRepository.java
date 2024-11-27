package com.superestoque.estoque.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.superestoque.estoque.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
	User findByEmail(@Param("email") String email);

	Optional<User> getByEmail(String email);
}