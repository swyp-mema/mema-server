package com.swyp.mema.domain.voteLocation.controller;

import com.swyp.mema.database.station.dto.response.TotalStationRes;
import com.swyp.mema.database.station.service.StationService;
import com.swyp.mema.domain.midloc.service.MidLocService;
import com.swyp.mema.domain.store.dto.naverMap.TotalStoreInfoRes;
import com.swyp.mema.domain.voteLocation.dto.response.MidLocationTotalRes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.mema.domain.voteLocation.dto.request.CreateLocationReq;
import com.swyp.mema.domain.voteLocation.dto.response.SingleLocationRes;
import com.swyp.mema.domain.voteLocation.service.LocationService;
import com.swyp.mema.domain.user.dto.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "위치", description = "위치 관련 API")
public class LocationController {

	private final LocationService locationService;
	private final MidLocService midLocationService;
	private final StationService stationService;

	/**
	 * 위치 투표 생성
	 */
	@Operation(summary = "위치 생성 API", description = "출발 위치를 생성할 수 있습니다.")
	@PostMapping("/meets/{meetId}/vote/location")
	public ResponseEntity<SingleLocationRes> save(
		@PathVariable(name="meetId") Long meetId,
		@Valid @RequestBody CreateLocationReq createLocationReq,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {

		Long userId = Long.parseLong(userDetails.getUsername());
		SingleLocationRes response = locationService.saveLocation(createLocationReq, meetId, userId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 내 위치 투표 조회
	 */
	@Operation(summary = "내 위치 조회 API", description = "내 출발 위치를 조회할 수 있습니다.")
	@GetMapping("/meets/{meetId}/vote/location/my")
	public ResponseEntity<SingleLocationRes> getMyLocation(
		@PathVariable(name="meetId") Long meetId,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = Long.parseLong(userDetails.getUsername());
		SingleLocationRes response = locationService.getMyLocation(meetId, userId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 전체 위치 조회 API
	 */
	@Operation(summary = "전체 위치 조회 API", description = "약속원의 전체 출발 위치와 중간 지점역을 조회할 수 있습니다.")
	@GetMapping("/meets/{meetId}/vote/location/total")
	public ResponseEntity<MidLocationTotalRes> getTotalLocation(
		@PathVariable(name="meetId") Long meetId,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getUserId();
		MidLocationTotalRes res = locationService.getTotalLocation(meetId, userId);
		return ResponseEntity.ok(res);
	}

	/**
	 * 맛집 추천 API
	 */
	@Operation(summary = "맛집 추천 API", description = "중간 지점 역 근처의 맛집을 추천받을 수 있다.")
	@GetMapping("/meets/{meetId}/recommend")
	public ResponseEntity<TotalStoreInfoRes> recommendStore(
		@PathVariable(name = "meetId") Long meetId,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getUserId();
		TotalStoreInfoRes response = locationService.recommendStore(userId, meetId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 모든 지하철역 조회 API
	 */
	@Operation(summary = "모든 지하철역 조회 API", description = "역DB 로 모든 지하철역 정보를 조회합니다.")
	@GetMapping("/station/total")
	public ResponseEntity<TotalStationRes> getAllStation(
			@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		TotalStationRes response = stationService.getStationInfo();
		return ResponseEntity.ok(response);
	}
}
