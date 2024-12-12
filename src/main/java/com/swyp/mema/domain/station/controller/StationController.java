package com.swyp.mema.domain.station.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.mema.domain.station.dto.response.TotalStationResponse;
import com.swyp.mema.domain.station.service.StationService;
import com.swyp.mema.domain.user.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StationController {

	private final StationService stationService;

	/**
	 * 모든 지하철역 조회 API
	 */
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
