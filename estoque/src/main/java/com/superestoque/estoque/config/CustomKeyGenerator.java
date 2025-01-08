package com.superestoque.estoque.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class CustomKeyGenerator {

	@Bean
	KeyGenerator keyGenerator() {
		return (target, method, params) -> target.getClass().getSimpleName() + "_" + method.getName() + "_"
				+ StringUtils.arrayToDelimitedString(params, "_");
	}
}