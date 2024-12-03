package com.swyp.mema.domain.meet.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.swyp.mema.domain.meet.dto.request.MeetNameReq;
import com.swyp.mema.domain.meet.dto.response.CreateMeetRes;
import com.swyp.mema.domain.meet.dto.response.MeetSingleRes;
import com.swyp.mema.domain.meet.service.MeetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "약속", description = "약속 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/meets")
public class MeetController {

	private final MeetService meetService;

	/**
	 * 약속 생성 API
	 */
	@Operation(summary = "약속 생성 API", description = "약속을 생성하면 해당 유저는 약속원으로 등록됩니다.")
	@PostMapping
	public ResponseEntity<CreateMeetRes> create(@Valid @RequestBody MeetNameReq meetNameReq) {

		CreateMeetRes response = meetService.create(meetNameReq);

		URI uri = UriComponentsBuilder
			.fromPath("/meets/{id}")
			.buildAndExpand( response.getMeetId())
			.toUri();

		return ResponseEntity.created(uri).body(response);
	}

	@Operation(summary = "약속 단건 조회 API", description = "약속에 대한 모든 정보를 조회할 수 있습니다.")
	@GetMapping("/{id}")
	public ResponseEntity<MeetSingleRes> getOne(
		@Parameter(description = "약속 ID", example = "1") @PathVariable("id") Long meetId) {
		MeetSingleRes response = meetService.getSingle(meetId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "약속 수정 API", description = "약속명을 수정할 수 있습니다.")
	@PatchMapping("/{id}")
	public ResponseEntity<MeetSingleRes> update(
		@Parameter(description = "약속 ID", example = "1") @PathVariable("id") Long meetId,
		@Valid @RequestBody MeetNameReq meetNameReq) {

		MeetSingleRes response = meetService.update(meetId, meetNameReq);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "약속 삭제 API", description = "약속을 삭제할 수 있습니다.")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMeet(
		@Parameter(description = "약속 ID", example = "1") @PathVariable("id") Long meetId) {
		meetService.delete(meetId);
		return ResponseEntity.noContent().build();
	}
}
