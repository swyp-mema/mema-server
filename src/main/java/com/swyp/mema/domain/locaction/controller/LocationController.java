package com.swyp.mema.domain.locaction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.mema.domain.locaction.dto.response.TotalStationResponse;
import com.swyp.mema.domain.locaction.service.LocationService;
import com.swyp.mema.domain.user.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LocationController {

	private final LocationService locationService;

	/**
	 * 모든 지하철역 조회 API
	 */
	@GetMapping("/meets/{meetId}/station/total")
	public ResponseEntity<TotalStationResponse> getAllStation(
		@PathVariable Long meetId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		Long userId = Long.parseLong(userDetails.getUsername());
		TotalStationResponse response = locationService.getSubwayInfo(meetId, userId);
		return ResponseEntity.ok(response);
	}
}
