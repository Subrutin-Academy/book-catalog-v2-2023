package com.subrutin.catalog.config;

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
import com.subrutin.catalog.security.filter.UsernamePasswordAuthFilter;
import com.subrutin.catalog.security.handler.UsernamePasswordAuthSuccessHandler;
import com.subrutin.catalog.security.handler.UserrnamePasswordAuthFalureHandler;
import com.subrutin.catalog.security.provider.UsernamePasswordAuthProvider;
import com.subrutin.catalog.service.AppUserService;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
	
	
	@Autowired
	private UsernamePasswordAuthProvider usernamePasswordAuthProvider;
	
	@Autowired
	void registerProvider(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
		auth.authenticationProvider(usernamePasswordAuthProvider);
	}
	
	

    @Bean
    AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    
	@Bean
	public UsernamePasswordAuthFilter usernamePasswordAuthFilter(
			UsernamePasswordAuthSuccessHandler successHandler, UserrnamePasswordAuthFalureHandler failureHandler,
			ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
		UsernamePasswordAuthFilter filter = new UsernamePasswordAuthFilter("/v1/login",
				successHandler, failureHandler, objectMapper);
		filter.setAuthenticationManager(authenticationManager);
		return filter;
	}
	
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, UsernamePasswordAuthFilter usernamePasswordAuthFilter) throws Exception {
		http.authorizeHttpRequests().anyRequest().authenticated()
		.and()
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.httpBasic();
		
        http.addFilterBefore(usernamePasswordAuthFilter, UsernamePasswordAuthenticationFilter.class);

//        .addFilterBefore(buildUsernamePasswordAuthFilter("/v1/login"), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

}
