package com.subrutin.catalog.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subrutin.catalog.security.filter.JwtAuthProcessingFilter;
import com.subrutin.catalog.security.filter.UsernamePasswordAuthProcessingFilter;
import com.subrutin.catalog.security.handler.UsernamePasswordAuthFailureHandler;
import com.subrutin.catalog.security.handler.UsernamePasswordAuthSucessHandler;
import com.subrutin.catalog.security.provider.JwtAuthenticationProvider;
import com.subrutin.catalog.security.provider.UsernamePasswordAuthProvider;
import com.subrutin.catalog.security.util.JWTTokenFactory;
import com.subrutin.catalog.security.util.SkipPathRequestMatcher;
import com.subrutin.catalog.service.AppUserService;
import com.subrutin.catalog.util.TokenExtractor;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
	
	private final static String AUTH_URL = "/v1/login";
	private final static String V1_URL = "/v1/*";
	private final static String V2_URL = "/v2/*";

	private final static List<String> PERMIT_ENDPOINT_LIST= Arrays.asList(AUTH_URL);
	private final static List<String> AUTHENTICATED_ENDPOINT_LIST= Arrays.asList(V1_URL, V2_URL);
	
	
	@Autowired
	private UsernamePasswordAuthProvider usernamePasswordAuthProvider;
	
	@Autowired
	private JwtAuthenticationProvider jwtAuthenticationProvider;
	
	@Bean
	public AuthenticationSuccessHandler successHandler(ObjectMapper objectMapper, JWTTokenFactory factory) {
		return new UsernamePasswordAuthSucessHandler(objectMapper, factory);
	}
	
	@Bean
	public AuthenticationFailureHandler failureHandler(ObjectMapper objectMapper) {
		return new UsernamePasswordAuthFailureHandler(objectMapper);
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public UsernamePasswordAuthProcessingFilter usernamePasswordAuthProcessingFilter(ObjectMapper objectMapper, 
			AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, AuthenticationManager authManager ) {
		UsernamePasswordAuthProcessingFilter filter = new UsernamePasswordAuthProcessingFilter(AUTH_URL, objectMapper, successHandler, failureHandler);
		filter.setAuthenticationManager(authManager);
		return filter;
	}
	
	@Bean
	public JwtAuthProcessingFilter jwtAuthProcessingFilter(TokenExtractor tokenExtractor, AuthenticationFailureHandler failureHandler,  AuthenticationManager authManager ) {
		SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(PERMIT_ENDPOINT_LIST, AUTHENTICATED_ENDPOINT_LIST);
		JwtAuthProcessingFilter filter = new JwtAuthProcessingFilter(matcher, tokenExtractor, failureHandler);
		filter.setAuthenticationManager(authManager);
		return filter;
	}
	
	@Autowired
	void registerProvider(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(usernamePasswordAuthProvider)
		.authenticationProvider(jwtAuthenticationProvider);
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, UsernamePasswordAuthProcessingFilter usernamePasswordAuthProcessingFilter,
			JwtAuthProcessingFilter jwtAuthProcessingFilter) throws Exception {
		http.authorizeHttpRequests().requestMatchers(V1_URL, V2_URL).authenticated()
		.and()
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.httpBasic();
		
		http.addFilterBefore(usernamePasswordAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class)
		.addFilterBefore(jwtAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

}
