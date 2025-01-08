package com.superestoque.estoque.services;

import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.superestoque.estoque.entities.Product;
import com.superestoque.estoque.entities.User;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

	private final JavaMailSender javaMailSender;

	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Async("taskExecutor")
	public void sendEmailProduct(Product product) {
		try {
			Set<String> adminEmails = product.getCompany().getUsers().stream()
					.filter(user -> user.getRoles().stream()
							.anyMatch(role -> role.getAuthority().equalsIgnoreCase("ROLE_ADMIN")))
					.map(User::getEmail).collect(Collectors.toSet());

			String[] recipients = adminEmails.toArray(new String[0]);

			String productDetails = """
					<h2>Atenção!</h2>
					<p>O produto <strong>%s</strong> entrou em quantidade crítica no estoque.</p>
					<p><strong>Quantidade Atual:</strong> %d</p>
					<p><strong>Quantidade Crítica:</strong> %d</p>
					<p><strong>Descrição:</strong> %s</p>
					<div class="product-image">
					    <img src="data:image/png;base64,%s" alt="Imagem do Produto">
					</div>
					<a href="http://137.131.180.24" class="button">Ver Estoque</a>
					""".formatted(product.getName(), product.getQuantity(), product.getCritical_quantity(),
					product.getDescription(), Base64.getEncoder().encodeToString(product.getPhoto()));

			String htmlContent = getEmailTemplate("Produto em Quantidade Crítica", productDetails);

			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(recipients);
			helper.setSubject("Produto com quantidade crítica");
			helper.setText(htmlContent, true);

			javaMailSender.send(mimeMessage);
			LOG.info("E-mail enviado com sucesso");
		} catch (Exception e) {
			LOG.error("Erro ao tentar enviar email {}", e.getLocalizedMessage());
		}
	}

	@Async("taskExecutor")
	public void sendPasswordResetEmail(User user, String token) {
		try {
			String resetLink = "http://137.131.180.24/reset-password?token=" + token;

			String resetDetails = """
					<p>Olá, %s!</p>
					<p>Recebemos um pedido para redefinir sua senha.</p>
					<p>Se você não solicitou essa alteração, por favor ignore este e-mail.</p>
					<p>Caso contrário, clique no link abaixo para redefinir sua senha:</p>
					<a href="%s" class="button">Redefinir Senha</a>
					<p>Este link expira em 15 minutos.</p>
					""".formatted(user.getName(), resetLink);

			String htmlContent = getEmailTemplate("Redefinição de Senha", resetDetails);

			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(user.getEmail());
			helper.setSubject("Redefinição de Senha");
			helper.setText(htmlContent, true);

			javaMailSender.send(mimeMessage);
			LOG.info("E-mail de redefinição de senha enviado para {}", user.getEmail());
		} catch (Exception e) {
			LOG.error("Erro ao enviar e-mail de redefinição de senha", e);
		}
	}

	private String getEmailTemplate(String title, String bodyContent) {
		return """
				<!DOCTYPE html>
				<html lang="pt-br">
				<head>
				<meta charset="UTF-8">
				<meta name="viewport" content="width=device-width, initial-scale=1.0">
				<style>
				    body {
				        font-family: 'Arial', sans-serif;
				        margin: 0;
				        padding: 0;
				        background-color: #eaf4fc;
				    }
				    .email-container {
				        background-color: #ffffff;
				        margin: 20px auto;
				        max-width: 600px;
				        border-radius: 8px;
				        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
				        overflow: hidden;
				    }

				    .header {
				        background-color: #408bd6;
				        color: #ffffff;
				        padding: 20px;
				        text-align: center;
				    }
				    .header h1 {
				        font-size: 24px;
				        margin: 0;
				    }

				    .content {
				        padding: 20px;
				        color: #333333;
				    }
				    .content h2 {
				        font-size: 20px;
				        margin-top: 0;
				    }
				    .content p {
				        margin: 10px 0;
				        font-size: 16px;
				    }

				    .button {
				        display: inline-block;
				        margin: 20px auto;
				        background-color: #408bd6;
				        color: #ffffff;
				        text-decoration: none;
				        padding: 10px 20px;
				        border-radius: 5px;
				        font-size: 16px;
				        text-align: center;
				    }
				    .button:hover {
				        background-color: #3072b3;
				    }

				    .footer {
				        text-align: center;
				        font-size: 12px;
				        color: #777777;
				        margin: 20px 0;
				        padding-bottom: 10px;
				    }
				</style>
				</head>
				<body>
				    <div class="email-container">
				        <div class="header">
				            <h1>%s</h1>
				        </div>
				        <div class="content">
				            %s
				        </div>
				        <div class="footer">
				            <p>Gerenciador de Estoque e Pedidos</p>
				            <p>Agilidade e precisão no seu estoque</p>
				        </div>
				    </div>
				</body>
				</html>
				""".formatted(title, bodyContent);
	}

}