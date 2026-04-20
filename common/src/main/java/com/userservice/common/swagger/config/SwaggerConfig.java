package com.userservice.common.swagger.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@Profile("!prod")
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("User Service API")
				.version("1.0")
				.description("API Documentation for User Service"))
			.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
			.components(new Components()
				.addSecuritySchemes("bearerAuth",
					new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
				))
			.servers(List.of(
				new Server().url("/").description("Current server")
			));
	}

	@Bean
	public GroupedOpenApi publicUserApi() {
		return GroupedOpenApi.builder()
			.group("Public User APIs")
			.pathsToMatch("/users/**")
			.build();
	}

	@Bean
	public GroupedOpenApi internalUserApi() {
		return GroupedOpenApi.builder()
			.group("Internal User APIs")
			.pathsToMatch("/internal/users/**")
			.build();
	}
}
