package com.swyp.mema.domain.voteDate.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "해당 미팅 첫 투표 생성")
public class CreateVoteDateReq {

	@Schema(description = "만료일", example = "2024-12-31T23:59:59")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@NotNull(message = "만료일은 필수 입력값입니다.")
	private LocalDateTime expiredVoteDate;	// LocalDateTime 변환

	@Schema(description = "약속원 ID", example = "1")
	@NotNull(message = "약속원 ID는 필수 입력값입니다.")
	private Long meetMemberId;

	@Schema(description = "가능한 투표일자", example = "['2024-12-18', '2024-12-19', '2024-12-20']")
	@NotEmpty(message = "투표 가능한 날짜를 선택해야 합니다.")
	private List<@NotNull LocalDate> voteDates; // 가능한 날짜 리스트

}
