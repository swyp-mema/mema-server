package com.swyp.mema.domain.locaction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.mema.domain.locaction.dto.request.CreateLocationReq;
import com.swyp.mema.domain.locaction.dto.response.SingleLocationResponse;
import com.swyp.mema.domain.locaction.dto.response.TotalLocationResponse;
import com.swyp.mema.domain.locaction.dto.response.TotalStationResponse;
import com.swyp.mema.domain.locaction.service.LocationByAPIService;
import com.swyp.mema.domain.locaction.service.LocationService;
import com.swyp.mema.domain.user.dto.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LocationController {

	private final LocationByAPIService locationByAPIService;
	private final LocationService locationService;

	/**
	 * 모든 지하철역 조회 API
	 */
	@GetMapping("/meets/{meetId}/station/total")
	public ResponseEntity<TotalStationResponse> getAllStation(
		@PathVariable Long meetId,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {

		Long userId = Long.parseLong(userDetails.getUsername());
		TotalStationResponse response = locationByAPIService.getSubwayInfo(meetId, userId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 위치 투표 생성
	 */
	@PostMapping("/meets/{meetId}/vote/location")
	public ResponseEntity<SingleLocationResponse> save(
		@PathVariable Long meetId,
		@Valid @RequestBody CreateLocationReq createLocationReq,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {

		Long userId = Long.parseLong(userDetails.getUsername());
		SingleLocationResponse response = locationService.saveLocation(createLocationReq, meetId, userId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 내 위치 투표 조회
	 */
	@GetMapping("/meets/{meetId}/vote/location/my")
	public ResponseEntity<SingleLocationResponse> getMyLocation(
		@PathVariable Long meetId,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = Long.parseLong(userDetails.getUsername());
		SingleLocationResponse response = locationService.getMyLocation(meetId, userId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 출발 위치 전체 조회
	 */
	@GetMapping("/meets/{meetId}/vote/location/total")
	public ResponseEntity<TotalLocationResponse> getTotalLocation(
		@PathVariable Long meetId,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = Long.parseLong(userDetails.getUsername());
		TotalLocationResponse response = locationService.getTotalLocation(meetId, userId);
		return ResponseEntity.ok(response);
	}

}
