package com.swyp.mema.domain.voteDate.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateVoteDateReq {

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime expiredVoteDate;	// LocalDateTime 변환

	@NotNull(message = "약속원 ID는 필수입니다.")
	private Long meetMemberId;

	@NotEmpty(message = "투표 가능한 날짜를 선택해야 합니다.")
	private List<@NotNull LocalDate> voteDates; // 가능한 날짜 리스트

}
