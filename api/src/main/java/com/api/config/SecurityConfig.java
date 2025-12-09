package com.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.headers(headers -> headers.frameOptions(frame -> frame.disable())) // H2 콘솔 & 프론트 iframe 접근 시
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/api/auth/login",
					"/h2-console/**"
				).permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable());

		return http.build();
	}
}
