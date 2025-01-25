package com.superestoque.estoque.services;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.Base64;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.superestoque.estoque.entities.Company;
import com.superestoque.estoque.entities.Product;
import com.superestoque.estoque.entities.Role;
import com.superestoque.estoque.entities.User;

import jakarta.mail.internet.MimeMessage;

class EmailServiceTests {

	@InjectMocks
	private EmailService emailService;

	@Mock
	private JavaMailSender javaMailSender;

	@Mock
	private MimeMessage mimeMessage;

	@Mock
	private MimeMessageHelper mimeMessageHelper;

	private Product product;
	private User adminUser;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		adminUser = new User();
		adminUser.setId(1L);
		adminUser.setName("Admin User");
		adminUser.setEmail("admin@example.com");
		Role adminRole = new Role(1L, "ROLE_ADMIN");
		adminUser.setRoles(Set.of(adminRole));

		Company company = new Company();
		company.setId(1L);
		company.getUsers().add(adminUser);

		product = new Product();
		product.setName("Product A");
		product.setQuantity(5);
		product.setCritical_quantity(10);
		product.setDescription("Critical product description");
		product.setPhoto(new byte[] { 1, 2, 3, 4 });
		product.setCompany(company);

		Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		Mockito.when(mimeMessageHelper.getMimeMessage()).thenReturn(mimeMessage);
	}

	@Test
	void sendEmailProductShouldSendEmailToAdmin() throws Exception {
		emailService.sendEmailProduct(product);

		String expectedPhotoBase64 = Base64.getEncoder().encodeToString(product.getPhoto());

		Mockito.verify(javaMailSender, times(1)).send(ArgumentMatchers.any(MimeMessage.class));
		Mockito.verify(mimeMessageHelper, never()).setTo(adminUser.getEmail());
		Mockito.verify(mimeMessageHelper, never()).setText(ArgumentMatchers.contains(expectedPhotoBase64), eq(true));
	}

	@Test
	void sendPasswordResetEmailShouldSendEmailToUser() throws Exception {
		String token = "testToken123";
		emailService.sendPasswordResetEmail(adminUser, token);

		String expectedResetLink = "http://137.131.180.24/reset-password?token=" + token;

		Mockito.verify(javaMailSender, times(1)).send(ArgumentMatchers.any(MimeMessage.class));
		Mockito.verify(mimeMessageHelper, never()).setTo(adminUser.getEmail());
		Mockito.verify(mimeMessageHelper, never()).setText(ArgumentMatchers.contains(expectedResetLink), eq(true));
	}
}
