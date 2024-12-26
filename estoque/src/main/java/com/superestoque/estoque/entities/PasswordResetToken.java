package com.superestoque.estoque.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_password_reset_token")
public class PasswordResetToken implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String token;

	@Column(nullable = false)
	private LocalDateTime expiryDate;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public PasswordResetToken() {
	}

	public PasswordResetToken(Long id, String token, LocalDateTime expiryDate, User user) {
		this.id = id;
		this.token = token;
		this.expiryDate = expiryDate;
		this.user = user;
	}

	public boolean isExpired() {
		return LocalDateTime.now().isAfter(this.expiryDate);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}