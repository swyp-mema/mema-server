package com.swyp.mema.domain.locaction.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp.mema.domain.locaction.dto.response.LocationResponse;

@Service
public class LocationService {

	@Value("${api.station.name.key}")
	private String key;

	private static final String BASE_URL = "http://openapi.seoul.go.kr:8088";
	private static final String TYPE = "json";
	private static final String SERVICE = "SearchSTNBySubwayLineInfo";
	private static final int START_INDEX = 1;
	private static final int END_INDEX = 1000;
	private static final String SUBWAY_NAME = "1호선";

	private final WebClient webClient;
	private final ObjectMapper objectMapper;

	public LocationService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
		this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
		this.objectMapper = objectMapper;

	}

	public LocationResponse getSubwayInfo() {

		String result = webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/{key}/{type}/{service}/{start}/{end}")
				.build(key, TYPE, SERVICE, START_INDEX, END_INDEX))
			.retrieve()
			.bodyToMono(String.class)
			.block();

		System.out.println("result = " + result);
		// Jackson 바인딩 에러로 인해 ObjectMapper 이용해 바인딩
		try {
			return objectMapper.readValue(result, LocationResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("JSON Parsing Error: " + e.getMessage(), e);
		}

	}
}
