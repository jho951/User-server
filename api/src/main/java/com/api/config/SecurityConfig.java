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

	// ✅ UserService에서 주입받을 PasswordEncoder 빈
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// ✅ 기본 SecurityFilterChain (H2 콘솔 허용 + 일단 전부 permitAll)
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.headers(headers ->
				// H2 콘솔 iframe 허용
				headers.frameOptions(frame -> frame.sameOrigin())
			)
			.authorizeHttpRequests(auth -> auth
				// H2 콘솔은 그냥 열어두고
				.requestMatchers("/h2-console/**").permitAll()
				// 나머지 API도 v1에서는 일단 전부 허용
				.anyRequest().permitAll()
			)
			// v1에서는 폼 로그인/기본 인증 비활성화 (프론트에서 fakeLogin 사용 중)
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable());

		return http.build();
	}
}
