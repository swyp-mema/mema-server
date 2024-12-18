package com.swyp.mema.global.base.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
public class BaseController {

	// 기본 경로 처리 (Root "/")
	@Hidden
	@GetMapping("/")
	public ResponseEntity<String> root() {
		return ResponseEntity.ok("Mema Service API: Success!");
	}
}
