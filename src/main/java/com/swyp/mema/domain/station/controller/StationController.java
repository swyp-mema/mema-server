package com.swyp.mema.domain.station.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.mema.domain.station.dto.response.TotalStationResponse;
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
}
