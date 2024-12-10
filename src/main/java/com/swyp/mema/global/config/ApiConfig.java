package com.swyp.mema.global.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class ApiConfig {

	@Bean
	public DefaultUriBuilderFactory builderFactory(){
		DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
		factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
		return factory;
	}

	@Bean
	public WebClient webClient(){
		return WebClient.builder()
			.uriBuilderFactory(builderFactory())
			.build();
	}

	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder()
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.defaultHeader(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
	}
}
