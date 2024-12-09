package com.swyp.mema.domain.locaction.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.mema.domain.locaction.service.LocationService;
import com.swyp.mema.domain.user.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LocationController {

	private final LocationService locationService;

	@GetMapping("/select/all")
	public void select() {
		System.out.println("여긴 오니");
		locationService.getSubwayInfo();
	}
}
