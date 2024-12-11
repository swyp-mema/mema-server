package com.swyp.mema.domain.locaction.service;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.swyp.mema.domain.locaction.converter.LocationConverter;
import com.swyp.mema.domain.locaction.dto.response.LocationResponse;
import com.swyp.mema.domain.locaction.dto.response.OpenApiBasicResponse;
import com.swyp.mema.domain.locaction.dto.response.TotalStationResponse;
import com.swyp.mema.domain.locaction.model.Station;
import com.swyp.mema.domain.locaction.repository.StationRepository;
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
public class LocationByAPIService {

	private static final String BASE_URL = "http://apis.data.go.kr/1613000/SubwayInfoService";
	private static final String SERVICE_NAME = "getKwrdFndSubwaySttnList";
	private static final String TYPE = "json";


	private final UserRepository userRepository;
	private final MeetRepository meetRepository;
	private final MeetMemberRepository meetMemberRepository;

	private final WebClient webClient;
	private final LocationConverter converter;
	private final StationRepository stationRepository;

	@Value("${api.key}")
	private String serviceKey;    // 디코딩된 API 서비스 키

	public LocationByAPIService(WebClient.Builder webClientBuilder, LocationConverter converter,
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
	public TotalStationResponse getSubwayInfo(Long meetId, Long userId) {

		// 필수 검증 로직
		User user = validateUser(userId);
		Meet meet = validateMeet(meetId);
		MeetMember meetMember = validateMeetMember(user, meet);
		validateMeetMember(meetMember.getId());

		List<Station> all = stationRepository.findAll();

		return converter.toTotalStationResponse(all);
	}


	/*
	 * OpenAPI 를 호출하여 Station 값 변경
	 */
	@Transactional
	public LocationResponse getSubwayInfoByAPI(Long meetId, Long userId) {

		// 필수 검증 로직
		User user = validateUser(userId);
		Meet meet = validateMeet(meetId);
		MeetMember meetMember = validateMeetMember(user, meet);
		validateMeetMember(meetMember.getId());

		// URI 빌더로 URL 생성
		URI uri = createUri();
		log.info("Generated URL: {}", uri);

		// OpenAPI 요청 및 JSON 확인
		OpenApiBasicResponse result = fetchSubwayInfoFromOpenApi(uri);
		log.info("result : {}", result);

		LocationResponse response = converter.toLocationResponse(result);

		List<Station> stations = response.getSubwayList().stream()
			.map(item -> new Station(
				item.getStationId(),
				item.getStationName(),
				item.getRouteName()
			))
			.toList();

		stationRepository.saveAll(stations);	// 대량 Insert

		return response;
	}


	private OpenApiBasicResponse fetchSubwayInfoFromOpenApi(URI uri) {

		// WebClient 요청
		return webClient.get()
			.uri(uri) // 생성한 URI를 그대로 사용
			.retrieve() // 응답 수신
			.bodyToMono(OpenApiBasicResponse.class)
			.block(); // 동기 방식으로 결과 받기
	}

	private URI createUri() {

		// URI 빌더로 URL 생성
		return UriComponentsBuilder.fromHttpUrl(BASE_URL)
			.pathSegment(SERVICE_NAME)
			.queryParam("serviceKey", serviceKey) // 자동으로 인코딩
			.queryParam("_type", TYPE) // 반환 타입
			// .queryParam("numOfRows", END_INDEX) // 최대 행 수
			// .queryParam("pageNo", START_INDEX) // 시작 페이지
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
