package com.superestoque.estoque.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI estoque() {
		return new OpenAPI().info(new Info().title("Estoque API")
				.description("Essa API tem como objetivo disponibilizar os dados.")
				.version("1.0"));
	}
}