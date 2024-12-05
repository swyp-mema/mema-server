package com.swyp.mema.domain.voteDate.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateVoteDateReq {

	@NotNull(message = "약속원 ID는 필수입니다.")
	private Long meetMemberId;

	@NotEmpty(message = "투표 가능한 날짜를 선택해야 합니다.")
	private List<@NotNull LocalDate> voteDates; // 수정할 날짜 리스트
}
