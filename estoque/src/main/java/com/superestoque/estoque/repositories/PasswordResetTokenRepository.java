package com.superestoque.estoque.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superestoque.estoque.entities.PasswordResetToken;
import com.superestoque.estoque.entities.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	Optional<PasswordResetToken> findByToken(String token);

	void deleteByUser(User user);

}