package com.swyp.mema.domain.voteDate.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.swyp.mema.domain.voteDate.dto.request.VoteDateReq;
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
	 * @param voteDateReq 투표 요청 데이터
	 */
	@PostMapping("/meets/{meetId}/vote/date")
	public ResponseEntity<TotalVoteDateListRes> createVote(
		@PathVariable Long meetId,
		@RequestBody @Valid VoteDateReq voteDateReq) {
		voteDateService.create(meetId, voteDateReq);
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
	@GetMapping("/meets/{meetId}/vote/date/my")
	public ResponseEntity<TotalVoteDateListRes> getVoteDates(@PathVariable Long meetId) {
		TotalVoteDateListRes response = voteDateService.getVoteDatesByMeetId(meetId);
		return ResponseEntity.ok(response);
	}
}
