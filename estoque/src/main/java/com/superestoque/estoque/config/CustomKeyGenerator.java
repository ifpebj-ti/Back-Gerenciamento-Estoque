package com.superestoque.estoque.config;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class CustomKeyGenerator {

	@Bean
	KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				return target.getClass().getSimpleName() + "_" + method.getName() + "_"
						+ StringUtils.arrayToDelimitedString(params, "_");
			}
		};
	}
}