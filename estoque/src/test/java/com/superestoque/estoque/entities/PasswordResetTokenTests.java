package com.superestoque.estoque.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class PasswordResetTokenTests {

	@Test
	void passwordResetTokenShouldHaveCorrectStructure() {
		User user = new User();
		user.setId(1L);

		PasswordResetToken token = new PasswordResetToken();
		token.setId(1L);
		token.setToken("sample-token");
		token.setExpiryDate(LocalDateTime.now().plusDays(1));
		token.setUser(user);

		Assertions.assertNotNull(token.getId());
		Assertions.assertEquals(1L, token.getId());
		Assertions.assertNotNull(token.getToken());
		Assertions.assertEquals("sample-token", token.getToken());
		Assertions.assertNotNull(token.getExpiryDate());
		Assertions.assertNotNull(token.getUser());
		Assertions.assertEquals(1L, token.getUser().getId());
	}

	@Test
	void constructorShouldInitializeFieldsCorrectly() {
		User user = new User();
		user.setId(1L);

		LocalDateTime expiryDate = LocalDateTime.now().plusDays(1);
		PasswordResetToken token = new PasswordResetToken(1L, "sample-token", expiryDate, user);

		Assertions.assertEquals(1L, token.getId());
		Assertions.assertEquals("sample-token", token.getToken());
		Assertions.assertEquals(expiryDate, token.getExpiryDate());
		Assertions.assertEquals(user, token.getUser());
	}

	@Test
	void isExpiredShouldReturnTrueWhenExpiryDateIsPast() {
		User user = new User();
		user.setId(1L);

		PasswordResetToken token = new PasswordResetToken(1L, "sample-token", LocalDateTime.now().minusDays(1), user);

		Assertions.assertTrue(token.isExpired());
	}

	@Test
	void isExpiredShouldReturnFalseWhenExpiryDateIsFuture() {
		User user = new User();
		user.setId(1L);

		PasswordResetToken token = new PasswordResetToken(1L, "sample-token", LocalDateTime.now().plusDays(1), user);

		Assertions.assertFalse(token.isExpired());
	}

	@Test
	void equalsShouldReturnTrueWhenIdAndTokenAreEqual() {
		User user = new User();
		user.setId(1L);

		PasswordResetToken token1 = new PasswordResetToken(1L, "sample-token", LocalDateTime.now().plusDays(1), user);
		PasswordResetToken token2 = new PasswordResetToken(1L, "sample-token", LocalDateTime.now().plusDays(1), user);

		Assertions.assertEquals(token1, token2);
	}

	@Test
	void equalsShouldReturnFalseWhenIdOrTokenAreDifferent() {
		User user = new User();
		user.setId(1L);

		PasswordResetToken token1 = new PasswordResetToken(1L, "sample-token-1", LocalDateTime.now().plusDays(1), user);
		PasswordResetToken token2 = new PasswordResetToken(2L, "sample-token-2", LocalDateTime.now().plusDays(1), user);

		Assertions.assertNotEquals(token1, token2);
	}

	@Test
	void equalsShouldReturnFalseWhenComparingWithNull() {
		PasswordResetToken token = new PasswordResetToken();
		token.setId(1L);

		Assertions.assertNotEquals(null, token);
	}

	@Test
	void equalsShouldReturnFalseWhenComparingWithDifferentClass() {
		PasswordResetToken token = new PasswordResetToken();
		token.setId(1L);

		String differentClassObject = "Different Class Object";

		Assertions.assertNotEquals(token, differentClassObject);
	}
}