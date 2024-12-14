package com.swyp.mema.domain.station.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.mema.domain.station.dto.response.nearSubway.TotalNearSubwayResponse;
import com.swyp.mema.domain.station.dto.response.subwayMaster.TotalSubwayMasterResponse;
import com.swyp.mema.domain.station.dto.response.subwayTime.TotalSubwayTimeResponse;
import com.swyp.mema.domain.station.dto.response.subwayInfo.TotalStationResponse;
import com.swyp.mema.domain.station.service.NearStationService;
import com.swyp.mema.domain.station.service.StationMasterService;
import com.swyp.mema.domain.station.service.StationService;
import com.swyp.mema.domain.user.dto.CustomUserDetails;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "위치", description = "위치 관련 API")
public class StationController {

	private final StationService stationService;
	private final NearStationService nearStationService;
	private final StationMasterService stationMasterService;

	/**
	 * 모든 지하철역 조회 API
	 */
	@Operation(summary = "모든 지하철역 조회 API", description = "역DB 로 모든 지하철역 정보를 조회합니다.")
	@GetMapping("/meets/{meetId}/station/total")
	public ResponseEntity<TotalStationResponse> getAllStation(
		@PathVariable Long meetId,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {

		Long userId = Long.parseLong(userDetails.getUsername());
		TotalStationResponse response = stationService.getSubwayInfo(meetId, userId);
		return ResponseEntity.ok(response);
	}

	/**
	 * openAPI 통해 지하철역 조회 API
	 */
	@Hidden
	@Operation(summary = "모든 지하철역 조회 API", description = "OpenAPI 로 모든 지하철역 정보를 조회합니다.")
	@GetMapping("/meets/{meetId}/station/all")
	public ResponseEntity<Void> getAllStationByOpenAPI(
		@PathVariable Long meetId,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {

		Long userId = Long.parseLong(userDetails.getUsername());
		stationService.getSubwayInfoByAPI(meetId, userId);
		return ResponseEntity.ok().build();
	}

	/**
	 * 이 API 는 무시해주세요!
	 * openAPI 통해 지하철역 시간표 조회 API
	 */
	@Hidden
	@Operation(summary = "해당 역에 대한 모든 시간표 조회 API", description = "OpenAPI 로 해당 '역 + 호선'에 대한 모든 시간표 정보를 조회합니다.",
		security = {})
	@GetMapping("/station/time")
	public ResponseEntity<TotalSubwayTimeResponse> getAllStationTimeByOpenAPI(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {

		String stationId = "MTRKRK1k210";	// 왕십리 수인분당선
		String dailyTypeCode = "01";		// 01 : 평일, 02 : 토요일, 03 : 일요일
		TotalSubwayTimeResponse response = stationService.getSubwayTimeByStationId(stationId, dailyTypeCode);
		return ResponseEntity.ok(response);
	}

	/**
	 * 이 API 는 무시해주세요!
	 * 지하철 실시간 시간 데이터 가져오는 API
	 */
	@Hidden
	@Operation(summary = "지하철 실시간 시간 API 통해 앞뒤 역 탐색", description = "지하철 실시간 시간 OpenAPI 로 앞뒤 역 정보를 조회합니다.",
		security = {})
	@GetMapping("/near/station")
	public ResponseEntity<TotalNearSubwayResponse> getNearStationTimeByOpenAPI(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {

		String stationName = "왕십리";
		TotalNearSubwayResponse response = nearStationService.getNearSubwayByAPI(stationName);
		return ResponseEntity.ok(response);
	}

	/**
	 * 이 API 는 무시해주세요!
	 * 지하철 실시간 시간 데이터 가져오는 API
	 */
	@Hidden
	@Operation(summary = "지하철 역사 마스터 API 통해 위도 & 경도 찾기", description = "지하철 역사 마스터 OpenAPI 로 해당 역의 위도 & 경도 값을 구합니다.",
		security = {})
	@GetMapping("/station/master")
	public ResponseEntity<TotalSubwayMasterResponse> getStationMasterByOpenAPI(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		TotalSubwayMasterResponse response = stationMasterService.getSubwayMasterByAPI();
		return ResponseEntity.ok(response);
	}
}
