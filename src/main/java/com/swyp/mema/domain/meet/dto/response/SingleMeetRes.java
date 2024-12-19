package com.swyp.mema.domain.meet.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swyp.mema.domain.meet.model.vo.State;
import com.swyp.mema.domain.meetMember.dto.response.MeetMemberRes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "약속 단건 조회 응답")
public class SingleMeetRes {

	@Schema(description = "약속 ID", example = "1")
	private Long meetId;

	@Schema(description = "약속 ID", example = "1")
	private int joinCode;

	@Schema(description = "약속명", example = "극 P들의 약속")
	private String meetName;

	@Schema(description = "약속 상태값", example = "CREATED")
	private State meetState;

	@Schema(description = "약속 날짜", example = "2025-01-10")
	private LocalDate meetDate;

	@Schema(description = "만나는 역 이름", example = "서울역")
	private String meetLocation;

	@Schema(description = "만나는 역 호선", example = "1호선")
	private String routeName;

	@Schema(description = "만나는 역 위도", example = "37.556228")
	private String lat;

	@Schema(description = "만나는 역 경도", example = "126.972135")
	private String lot;

	@Schema(description = "날짜 투표 만료일", example = "2025-01-01T02:11:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")	// 직렬화
	private LocalDateTime voteExpiredDate;

	@Schema(description = "해당 약속에 참여하는 유저 정보")
	private List<MeetMemberRes> members;
}
