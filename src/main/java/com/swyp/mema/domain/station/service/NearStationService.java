package com.swyp.mema.domain.station.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.swyp.mema.domain.station.converter.StationConverter;
import com.swyp.mema.domain.station.dto.response.nearSubway.NearSubwayBasicResponse;
import com.swyp.mema.domain.station.dto.response.nearSubway.TotalNearSubwayResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NearStationService {

	private static final String BASE_URL = "http://swopenAPI.seoul.go.kr/api/subway";
	private static final String NEAR_SUBWAY_SERVICE_NAME = "realtimeStationArrival";
	private static final String TYPE = "json";

	private static final String START_INDEX = "1";
	private static final String END_INDEX = "100";

	private final WebClient webClient;
	private final StationConverter converter;

	@Value("${api.time.key}")
	private String serviceKey;    // 디코딩된 API 서비스 키

	public NearStationService(WebClient.Builder webClientBuilder, StationConverter converter) {
		this.webClient = webClientBuilder.baseUrl(BASE_URL).build(); // 기본 URL 설정
		this.converter = converter;
	}

	/*
	 * 지하철 실시간 시간 데이터 OpenAPI 를 호출하여 현재역 기준 앞뒤 역 데이터를 가져오기
	 */
	@Transactional
	public TotalNearSubwayResponse getNearSubwayByAPI(String stationName) {

		// 서울역인 경우 서울로 변경
		stationName = stationName.equals("서울역") ? "서울" : stationName;

		// URI 빌더로 URL 생성
		URI uri = createNearSubwayUri(stationName);
		log.info("Generated SubwayInfo API Request URL: {}", uri);

		// OpenAPI 요청 및 JSON 확인
		NearSubwayBasicResponse result = fetchOpenApiForNearSubway(uri);
		log.info("result : {}", result);
		return converter.toNearSubwayBasicResponse(result);
	}

	private URI createNearSubwayUri(String stationName) {

		return UriComponentsBuilder.fromHttpUrl(BASE_URL)
			.pathSegment(serviceKey, TYPE, NEAR_SUBWAY_SERVICE_NAME, START_INDEX, END_INDEX, stationName)
			.encode() // 자동 인코딩 (한글은 인코딩되지 않음)
			.build()
			.toUri();
	}

	private NearSubwayBasicResponse fetchOpenApiForNearSubway(URI uri) {

		// WebClient 요청
		return webClient.get()
			.uri(uri) // 생성한 URI를 그대로 사용
			.retrieve() // 응답 수신
			.bodyToMono(NearSubwayBasicResponse.class)
			.block(); // 동기 방식으로 결과 받기
	}
}
