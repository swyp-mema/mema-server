package com.swyp.mema.domain.meet.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.swyp.mema.domain.meet.dto.request.JoinMeetReq;
import com.swyp.mema.domain.meet.dto.request.MeetNameReq;
import com.swyp.mema.domain.meet.dto.response.CreateMeetRes;
import com.swyp.mema.domain.meet.dto.response.SingleMeetRes;
import com.swyp.mema.domain.meet.service.MeetService;
import com.swyp.mema.domain.user.dto.CustomUserDetails;

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
	public ResponseEntity<CreateMeetRes> create(
		@Valid @RequestBody MeetNameReq meetNameReq,
		@AuthenticationPrincipal CustomUserDetails user) {

		Long userId = Long.parseLong(user.getUsername());
		CreateMeetRes response = meetService.create(meetNameReq, userId);

		URI uri = UriComponentsBuilder
			.fromPath("/meets/{meetId}")
			.buildAndExpand(response.getMeetId())
			.toUri();

		return ResponseEntity.created(uri).body(response);
	}

	/**
	 * 약속 참여 코드로 약속원 등록
	 */
	@Operation(summary = "참여 코드를 통해 약속 참여 API", description = "생성된 약속을 참여코드를 통해 참여할 수 있습니다.")
	@PostMapping("/join")
	public ResponseEntity<SingleMeetRes> joinMeet(
		@Valid @RequestBody JoinMeetReq joinMeetReq,
		@AuthenticationPrincipal CustomUserDetails user
	) {

		Long userId = Long.parseLong(user.getUsername());
		SingleMeetRes response = meetService.join(joinMeetReq, userId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "약속 단건 조회 API", description = "약속에 대한 모든 정보를 조회할 수 있습니다.")
	@GetMapping("/{meetId}")
	public ResponseEntity<SingleMeetRes> getOne(
		@Parameter(description = "약속 ID", example = "1") @PathVariable Long meetId,
		@AuthenticationPrincipal CustomUserDetails user) {

		Long userId = Long.parseLong(user.getUsername());
		SingleMeetRes response = meetService.getSingle(meetId, userId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "약속 수정 API", description = "약속명을 수정할 수 있습니다.")
	@PatchMapping("/{meetId}")
	public ResponseEntity<SingleMeetRes> update(
		@Parameter(description = "약속 ID", example = "1") @PathVariable Long meetId,
		@Valid @RequestBody MeetNameReq meetNameReq,
		@AuthenticationPrincipal CustomUserDetails user) {

		Long userId = Long.parseLong(user.getUsername());
		SingleMeetRes response = meetService.update(meetId, meetNameReq, userId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "약속 삭제 API", description = "약속을 삭제할 수 있습니다.")
	@DeleteMapping("/{meetId}")
	public ResponseEntity<Void> deleteMeet(
		@Parameter(description = "약속 ID", example = "1") @PathVariable Long meetId,
		@AuthenticationPrincipal CustomUserDetails user) {

		Long userId = Long.parseLong(user.getUsername());
		meetService.delete(meetId, userId);
		return ResponseEntity.noContent().build();
	}
}
