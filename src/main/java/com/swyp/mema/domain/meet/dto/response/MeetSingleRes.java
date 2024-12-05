package com.swyp.mema.domain.meet.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.swyp.mema.domain.meet.model.vo.State;
import com.swyp.mema.domain.meetMember.dto.response.MeetMemberRes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "약속 단건 조회 응답")
public class MeetSingleRes {

	@Schema(description = "약속 ID", example = "1")
	private Long meetId;

	@Schema(description = "약속명", example = "극 P들의 약속")
	private String meetName;

	@Schema(description = "약속 상태값", example = "CANCEL")
	private State meetState;

	@Schema(description = "약속 날짜", example = "2024-12-25")
	private LocalDateTime meetDate;

	@Schema(description = "약속 장소", example = "서울역")
	private String meetLocation;

	@Schema(description = "날짜 투표 만료일", example = "2024-12-10")
	private LocalDateTime voteExpiredDate;

	@Schema(description = "장소 투표 만료일", example = "2024-12-15")
	private LocalDateTime voteExpiredLocation;

	@Schema(description = "해당 약속에 참여하는 유저 정보")
	private List<MeetMemberRes> members;
}
