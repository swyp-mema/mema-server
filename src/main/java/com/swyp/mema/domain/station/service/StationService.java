package com.swyp.mema.domain.station.service;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.swyp.mema.domain.station.converter.StationConverter;
import com.swyp.mema.domain.station.dto.response.subwayInfo.SubwayInfoBasicResponse;
import com.swyp.mema.domain.station.dto.response.subwayTime.SubwayTimeBasicResponse;
import com.swyp.mema.domain.station.dto.response.subwayTime.TotalSubwayTimeResponse;
import com.swyp.mema.domain.station.dto.response.subwayInfo.TotalStationResponse;
import com.swyp.mema.domain.station.model.Station;
import com.swyp.mema.domain.station.repository.StationRepository;
import com.swyp.mema.domain.meet.exception.MeetNotFoundException;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.repository.MeetRepository;
import com.swyp.mema.domain.meetMember.exception.MeetMemberNotFoundException;
import com.swyp.mema.domain.meetMember.exception.NotMeetMemberException;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.user.exception.UserNotFoundException;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StationService {

	private static final String BASE_URL = "http://apis.data.go.kr/1613000/SubwayInfoService";
	private static final String SUBWAY_INFO_SERVICE_NAME = "getKwrdFndSubwaySttnList";
	private static final String SUBWAY_TIME_SERVICE_NAME = "getSubwaySttnAcctoSchdulList";
	private static final String TYPE = "json";


	private final UserRepository userRepository;
	private final MeetRepository meetRepository;
	private final MeetMemberRepository meetMemberRepository;

	private final WebClient webClient;
	private final StationConverter converter;
	private final StationRepository stationRepository;

	@Value("${api.info.key}")
	private String serviceKey;    // 디코딩된 API 서비스 키

	public StationService(WebClient.Builder webClientBuilder, StationConverter converter,
		UserRepository userRepository, MeetRepository meetRepository,
		MeetMemberRepository meetMemberRepository, StationRepository stationRepository) {

		this.webClient = webClientBuilder.baseUrl(BASE_URL).build(); // 기본 URL 설정
		this.converter = converter;
		this.userRepository = userRepository;
		this.meetRepository = meetRepository;
		this.meetMemberRepository = meetMemberRepository;
		this.stationRepository = stationRepository;
	}

	/**
	 * Station 테이블의 값을 가져와서 모든 지하철 조회
	 * Station 테이블 저장
	 */
	@Transactional(readOnly = true)
	public TotalStationResponse getSubwayInfo() {

		List<Station> all = stationRepository.findAll();

		return converter.toTotalStationResponse(all);
	}
	
	/*
	 * OpenAPI 를 호출하여 Station 값 변경
	 */
	// @Transactional
	// public void getSubwayInfoByAPI(Long meetId, Long userId) {
	//
	// 	// 필수 검증 로직
	// 	User user = validateUser(userId);
	// 	Meet meet = validateMeet(meetId);
	// 	MeetMember meetMember = validateMeetMember(user, meet);
	// 	validateMeetMember(meetMember.getId());
	//
	// 	// URI 빌더로 URL 생성
	// 	URI uri = createSubwayInfoUri();
	// 	log.info("Generated SubwayInfo API Request URL: {}", uri);
	//
	// 	// OpenAPI 요청 및 JSON 확인
	// 	SubwayInfoBasicResponse result = fetchOpenApiForSubwayInfo(uri);
	// 	log.info("result : {}", result);
	//
	//
	// 	List<Station> stations = result.getResponse().getBody().getItems().getItemList().stream()
	// 		.map(item -> new Station(
	// 			item.getStationName(),
	// 			item.getRouteName())
	// 		)
	// 		.toList();
	//
	// 	stationRepository.saveAll(stations);	// 대량 Insert
	//
	// }

	/*
	 * 해당 역ID(역이름 + 호선 정보) 통해 해당 호선에 대한 모든 시간 데이터 요청 OpenAPI
	 * */
	@Transactional
	public TotalSubwayTimeResponse getSubwayTimeByStationId(String stationId, String dailyTypeCode) {

		// 필수 검증 로직
		// User user = validateUser(userId);
		// Meet meet = validateMeet(meetId);
		// MeetMember meetMember = validateMeetMember(user, meet);
		// validateMeetMember(meetMember.getId());

		// URI 빌더로 URL 생성
		System.out.println("serviceKey = " + serviceKey);
		URI uri = createSubwayTimeUri(stationId, dailyTypeCode);
		log.info("Generated SubwayTime API Request URL: {}", uri);

		// OpenAPI 요청 및 JSON 확인
		SubwayTimeBasicResponse result = fetchOpenApiForSubwayTime(uri);
		log.info("result : {}", result);

		return converter.toSubwayTimeListResponse(result);
	}

	private SubwayTimeBasicResponse fetchOpenApiForSubwayTime(URI uri) {

		// WebClient 요청
		return webClient.get()
			.uri(uri) // 생성한 URI를 그대로 사용
			.retrieve() // 응답 수신
			.bodyToMono(SubwayTimeBasicResponse.class)
			.block(); // 동기 방식으로 결과 받기
	}

	private SubwayInfoBasicResponse fetchOpenApiForSubwayInfo(URI uri) {

		// WebClient 요청
		return webClient.get()
			.uri(uri) // 생성한 URI를 그대로 사용
			.retrieve() // 응답 수신
			.bodyToMono(SubwayInfoBasicResponse.class)
			.block(); // 동기 방식으로 결과 받기
	}

	private URI createSubwayInfoUri() {

		// URI 빌더로 URL 생성
		return UriComponentsBuilder.fromHttpUrl(BASE_URL)
			.pathSegment(SUBWAY_INFO_SERVICE_NAME)
			.queryParam("serviceKey", serviceKey) // 자동으로 인코딩
			.queryParam("_type", TYPE) // 반환 타입
			.queryParam("numOfRows", 1200) // 최대 행 수
			// .queryParam("pageNo", START_INDEX) // 시작 페이지
			.build()
			.toUri();
	}

	private URI createSubwayTimeUri(String stationId, String dailyTypeCode) {

		// URI 빌더로 URL 생성
		return UriComponentsBuilder.fromHttpUrl(BASE_URL)
			.pathSegment(SUBWAY_TIME_SERVICE_NAME)
			.queryParam("serviceKey", serviceKey) // 자동으로 인코딩
			.queryParam("subwayStationId", stationId)
			.queryParam("dailyTypeCode", dailyTypeCode)
			.queryParam("_type", TYPE) // 반환 타입
			.queryParam("upDownTypeCode", "U")	// U : 상행, D : 하행
			.queryParam("numOfRows", 500) // 최대 행 수
			// .queryParam("pageNo", 0) // 시작 페이지
			.build()
			.toUri();

	}

	private MeetMember validateMeetMember(User user, Meet meet) {
		return meetMemberRepository.findByUserAndMeet(user, meet)
			.orElseThrow(NotMeetMemberException::new);
	}

	private MeetMember validateMeetMember(Long meetMemberId) {
		return meetMemberRepository.findById(meetMemberId)
			.orElseThrow(MeetMemberNotFoundException::new);
	}

	private Meet validateMeet(Long meetId) {
		return meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);
	}

	private User validateUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
	}
}
