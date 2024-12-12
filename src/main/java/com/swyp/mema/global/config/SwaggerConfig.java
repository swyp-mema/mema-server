package com.swyp.mema.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {

		// SecurityScheme 정의
		SecurityScheme securityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.description("Enter your Bearer token in the value field");

		// SecurityRequirement 정의 (전역 적용)
		SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

		return new OpenAPI()
			.info(new Info()
				.title("Mema API")
				.version("1.0")
				.description("Mema 프로젝트를 위한 API 문서"))
			.addSecurityItem(securityRequirement) // 전역 SecurityRequirement 추가
			.components(new io.swagger.v3.oas.models.Components()
				.addSecuritySchemes("BearerAuth", securityScheme))
			.tags(List.of( // 태그 추가
				new Tag().name("사용자").description("사용자 관련 API"),
				new Tag().name("위치").description("위치 관련 API")
			)
			);
	}
}
