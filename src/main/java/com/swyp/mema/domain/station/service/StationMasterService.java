package com.swyp.mema.domain.station.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.swyp.mema.domain.station.converter.StationConverter;
import com.swyp.mema.domain.station.dto.response.subwayMaster.SubwayMasterBasicResponse;
import com.swyp.mema.domain.station.dto.response.subwayMaster.SubwayMasterResponse;
import com.swyp.mema.domain.station.dto.response.subwayMaster.TotalSubwayMasterResponse;
import com.swyp.mema.domain.station.model.Station;
import com.swyp.mema.domain.station.repository.StationRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StationMasterService {

	private static final String BASE_URL = "http://openapi.seoul.go.kr:8088";
	private static final String SUBWAY_MASTER_SERVICE_NAME = "subwayStationMaster";
	private static final String TYPE = "json";

	private static final String START_INDEX = "1";
	private static final String END_INDEX = "800";

	private final WebClient webClient;
	private final StationConverter converter;
	private final StationRepository repository;

	@Value("${api.master.key}")
	private String serviceKey;    // 디코딩된 API 서비스 키

	public StationMasterService(WebClient.Builder webClientBuilder, StationConverter converter, StationRepository repository) {
		this.webClient = webClientBuilder.baseUrl(BASE_URL).build(); // 기본 URL 설정
		this.converter = converter;
		this.repository = repository;
	}

	/*
	 * 지하철 역사 마스터 (위도 & 경도) 데이터 가져오는 메서드
	 */
	@Transactional
	public TotalSubwayMasterResponse getSubwayMasterByAPI() {

		// URI 빌더로 URL 생성
		URI uri = createSubwayMasterUri();
		log.info("Generated SubwayInfo API Request URL: {}", uri);

		// OpenAPI 요청 및 JSON 확인
		SubwayMasterBasicResponse result = fetchOpenApiForSubwayMaster(uri);
		log.info("result : {}", result);

		List<SubwayMasterResponse> responses = converter.toSubwayMasterListResponse(result);

		List<Station> stations = responses.stream()
			.map(m -> new Station(
				m.getStationName(),
				m.getRoute(),
				m.getLat(),
				m.getLot()
			)).toList();

		repository.saveAll(stations);	// 대량 Insert

		return converter.toTotalSubwayMasterResponse(responses);

	}

	private URI createSubwayMasterUri() {

		return UriComponentsBuilder.fromHttpUrl(BASE_URL)
			.pathSegment(serviceKey, TYPE, SUBWAY_MASTER_SERVICE_NAME, START_INDEX, END_INDEX)
			.build()
			.toUri();
	}

	private SubwayMasterBasicResponse fetchOpenApiForSubwayMaster(URI uri) {

		// WebClient 요청
		return webClient.get()
			.uri(uri) // 생성한 URI를 그대로 사용
			.retrieve() // 응답 수신
			.bodyToMono(SubwayMasterBasicResponse.class)
			.block(); // 동기 방식으로 결과 받기
	}
}
