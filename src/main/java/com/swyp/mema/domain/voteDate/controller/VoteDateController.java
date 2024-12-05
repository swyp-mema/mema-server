package com.swyp.mema.domain.voteDate.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.swyp.mema.domain.voteDate.dto.request.CreateVoteDateReq;
import com.swyp.mema.domain.voteDate.dto.request.FinalVoteDateReq;
import com.swyp.mema.domain.voteDate.dto.request.UpdateVoteDateReq;
import com.swyp.mema.domain.voteDate.dto.response.TotalVoteDateListRes;
import com.swyp.mema.domain.voteDate.service.VoteDateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class VoteDateController {

	private final VoteDateService voteDateService;

	/**
	 * 날짜 투표 생성
	 * @param meetId 약속 ID
	 * @param createVoteDateReq 투표 요청 데이터
	 */
	@PostMapping("/meets/{meetId}/vote/date")
	public ResponseEntity<TotalVoteDateListRes> createVote(
		@PathVariable Long meetId,
		@RequestBody @Valid CreateVoteDateReq createVoteDateReq) {
		voteDateService.create(meetId, createVoteDateReq);
		// 날짜 투표 생성 후 약속의 전체 투표 데이터를 반환
		TotalVoteDateListRes response = voteDateService.getVoteDatesByMeetId(meetId);

		URI uri = UriComponentsBuilder
			.fromPath("/meets/{meetId}/vote/date")
			.buildAndExpand(meetId)
			.toUri();

		return ResponseEntity.created(uri).body(response);
	}

	/**
	 * 약속 ID 기준으로 날짜 투표 조회
	 * @param meetId 약속 ID
	 * @return 날짜별 투표 데이터
	 */
	@GetMapping("/meets/{meetId}/vote/date/total")
	public ResponseEntity<TotalVoteDateListRes> getVoteDates(@PathVariable Long meetId) {
		TotalVoteDateListRes response = voteDateService.getVoteDatesByMeetId(meetId);
		return ResponseEntity.ok(response);
	}

	/*
	* PR MERGE 후 작성하기*/
	// @GetMapping("/meets/{meetId}/vote/date/my")
	// public ResponseEntity<SingleVoteDateRes> getMyVoteDates(@PathVariable Long meetId) {
	// 	SingleVoteDateRes voteDates = voteDateService.getVoteDatesByMeetMemberId(meetId);
	// 	return ResponseEntity.ok(voteDates);
	// }

	/**
	 * 날짜 투표 수정
	 * @param meetId 약속 ID
	 * @param voteDateReq 날짜 투표 요청 DTO
	 */
	@PatchMapping("/meets/{meetId}/vote/date")
	public ResponseEntity<TotalVoteDateListRes> updateVoteDates(
		@PathVariable Long meetId,
		@Valid @RequestBody UpdateVoteDateReq voteDateReq
	) {
		TotalVoteDateListRes response = voteDateService.updateVoteDates(meetId, voteDateReq);
		return ResponseEntity.ok(response);
	}

	/**
	 * 최종 날짜 설정
	 * @param meetId 약속 ID
	 * @param finalVoteDateReq 최종 날짜 요청 DTO
	 */
	@PatchMapping("/meets/{meetId}/vote/date/final")
	public ResponseEntity<Void> setFinalVoteDate(
		@PathVariable Long meetId,
		@Valid @RequestBody FinalVoteDateReq finalVoteDateReq
	) {
		voteDateService.setFinalVoteDate(meetId, finalVoteDateReq);
		return ResponseEntity.ok().build(); // 성공 시 200 OK 반환
	}
}
