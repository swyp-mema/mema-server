package com.swyp.mema.domain.voteDate.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.swyp.mema.domain.user.dto.CustomUserDetails;
import com.swyp.mema.domain.voteDate.dto.request.CreateVoteDateReq;
import com.swyp.mema.domain.voteDate.dto.request.FinalVoteDateReq;
import com.swyp.mema.domain.voteDate.dto.request.UpdateVoteDateReq;
import com.swyp.mema.domain.voteDate.dto.response.SingleVoteDateRes;
import com.swyp.mema.domain.voteDate.dto.response.TotalVoteDateListRes;
import com.swyp.mema.domain.voteDate.service.VoteDateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "일정", description = "일정 관련 API")
public class VoteDateController {

	private final VoteDateService voteDateService;

	/**
	 * 날짜 투표 생성
	 * @param meetId 약속 ID
	 * @param createVoteDateReq 투표 요청 데이터
	 */
	@Operation(summary = "일정 생성 API", description = "특정 약속의 일정을 생성합니다.")
	@PostMapping("/meets/{meetId}/vote/date")
	public ResponseEntity<TotalVoteDateListRes> createVote(
		@PathVariable Long meetId,
		@RequestBody @Valid CreateVoteDateReq createVoteDateReq,
		@AuthenticationPrincipal CustomUserDetails user) {

		Long userId = Long.parseLong(user.getUsername());

		// 만료일이 있는 경우: 투표 생성
		if (createVoteDateReq.getExpiredVoteDate() != null) {
			voteDateService.createVote(meetId, createVoteDateReq, userId);
		}
		// 만료일이 없는 경우: 투표 참여
		else {
			voteDateService.participateVote(meetId, createVoteDateReq, userId);
		}

		URI uri = UriComponentsBuilder
			.fromPath("/meets/{meetId}/vote/date")
			.buildAndExpand(meetId)
			.toUri();

		// 날짜 투표 생성 후 약속의 전체 투표 데이터를 반환
		TotalVoteDateListRes response = voteDateService.getVoteDatesByMeetId(meetId, userId);

		return ResponseEntity.created(uri).body(response);
	}

	/**
	 * 약속 ID 기준으로 날짜 투표 조회
	 * @param meetId 약속 ID
	 * @return 날짜별 투표 데이터
	 */
	@Operation(summary = "일정 전체 조회 API", description = "특정 약속의 약속원들이 입력한 일정을 모두 조회할 수 있습니다.")
	@GetMapping("/meets/{meetId}/vote/date/total")
	public ResponseEntity<TotalVoteDateListRes> getVoteDates(
		@PathVariable Long meetId,
		@AuthenticationPrincipal CustomUserDetails user) {

		Long userId = Long.parseLong(user.getUsername());
		TotalVoteDateListRes response = voteDateService.getVoteDatesByMeetId(meetId, userId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 내 날짜 투표 조회
	 */
	@Operation(summary = "내 일정 조회 API", description = "특정 약속에서 입력한 나의 일정을 조회할 수 있습니다.")
	@GetMapping("/meets/{meetId}/vote/date/my")
	public ResponseEntity<SingleVoteDateRes> getMyVoteDates(
		@PathVariable Long meetId,
		@AuthenticationPrincipal CustomUserDetails user) {

		Long userId = Long.parseLong(user.getUsername());
		SingleVoteDateRes voteDates = voteDateService.getVoteDatesByMeetMemberId(meetId, userId);
		return ResponseEntity.ok(voteDates);
	}

	/**
	 * 날짜 투표 수정
	 * @param meetId 약속 ID
	 * @param voteDateReq 날짜 투표 요청 DTO
	 */
	@Operation(summary = "일정 수정 API", description = "특정 약속에서 입력한 나의 일정을 수정할 수 있습니다.")
	@PatchMapping("/meets/{meetId}/vote/date")
	public ResponseEntity<TotalVoteDateListRes> updateVoteDates(
		@PathVariable Long meetId,
		@Valid @RequestBody UpdateVoteDateReq voteDateReq,
		@AuthenticationPrincipal CustomUserDetails user) {

		Long userId = Long.parseLong(user.getUsername());
		voteDateService.updateVoteDates(meetId, userId, voteDateReq);

		// 날짜 투표 생성 후 약속의 전체 투표 데이터를 반환
		TotalVoteDateListRes response = voteDateService.getVoteDatesByMeetId(meetId, userId);

		return ResponseEntity.ok(response);
	}

	/**
	 * 최종 날짜 설정
	 * @param meetId 약속 ID
	 * @param finalVoteDateReq 최종 날짜 요청 DTO
	 */
	@Operation(summary = "최종 날짜 선택 API", description = "최종 날짜를 선택할 수 있습니다.")
	@PatchMapping("/meets/{meetId}/vote/date/final")
	public ResponseEntity<Void> setFinalVoteDate(
		@PathVariable Long meetId,
		@Valid @RequestBody FinalVoteDateReq finalVoteDateReq,
		@AuthenticationPrincipal CustomUserDetails user) {

		Long userId = Long.parseLong(user.getUsername());
		voteDateService.setFinalVoteDate(meetId, userId, finalVoteDateReq);
		return ResponseEntity.ok().build(); // 성공 시 200 OK 반환
	}

	@Operation(summary = "최종 날짜 선택 API", description = "최종 날짜를 선택할 수 있습니다.")
	@PatchMapping("/meets/{meetId}/vote/date/final")
	public ResponseEntity<Void> deleteVodeDate(
			@PathVariable Long meetId,
			@AuthenticationPrincipal CustomUserDetails user	){

		Long userId = Long.parseLong(user.getUsername());
		voteDateService.deleteVote(meetId, userId);
		return ResponseEntity.noContent().build();
	}
}
