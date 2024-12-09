package com.swyp.mema.global.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry corsRegistry) {

		corsRegistry.addMapping("/**")
			.exposedHeaders("Set-Cookie", "Authorization")
			.allowedOrigins("http://localhost:3000")
			.allowedMethods("OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
			.allowedHeaders("*") // 모든 헤더 허용
			.allowCredentials(true); // 인증 정보 허용 (쿠키 등)
		//                .allowedOrigins("http://192.168.45.162:3000");
	}
}
