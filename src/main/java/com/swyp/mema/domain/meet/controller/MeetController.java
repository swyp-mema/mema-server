package com.swyp.mema.domain.meet.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.swyp.mema.domain.meet.dto.request.CreateMeetReq;
import com.swyp.mema.domain.meet.dto.response.CreateMeetResponse;
import com.swyp.mema.domain.meet.dto.response.MeetSingleResponse;
import com.swyp.mema.domain.meet.service.MeetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meets")
public class MeetController {

	private final MeetService meetService;

	/**
	 * 약속 생성 API
	 */
	@PostMapping
	public ResponseEntity<CreateMeetResponse> create(@Valid @RequestBody CreateMeetReq createMeetReq) {

		CreateMeetResponse response = meetService.create(createMeetReq);

		URI uri = UriComponentsBuilder
			.fromPath("/meets/{id}")
			.buildAndExpand( response.getMeetId())
			.toUri();

		return ResponseEntity.created(uri).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MeetSingleResponse> getOne(@PathVariable("id") Long id) {
		MeetSingleResponse response = meetService.getSingle(id);
		return ResponseEntity.ok(response);
	}
}
