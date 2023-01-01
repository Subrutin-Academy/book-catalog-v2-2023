package com.subrutin.catalog.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subrutin.catalog.domain.Author;
import com.subrutin.catalog.domain.Book;
import com.subrutin.catalog.repository.BookRepository;
import com.subrutin.catalog.repository.impl.BookRepositoryImpl;
import com.subrutin.catalog.security.filter.UsernamePasswordAuthFilter;
import com.subrutin.catalog.security.handler.UsernamePasswordAuthSuccessHandler;
import com.subrutin.catalog.security.handler.UserrnamePasswordAuthFalureHandler;

@Configuration
public class AppConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


    
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public UsernamePasswordAuthSuccessHandler successHandler(ObjectMapper objectMapper) {
		return new UsernamePasswordAuthSuccessHandler(objectMapper);
	}
	
	@Bean
	public UserrnamePasswordAuthFalureHandler failureHandler(ObjectMapper objectMapper) {
		return new UserrnamePasswordAuthFalureHandler(objectMapper);
	}

	

}