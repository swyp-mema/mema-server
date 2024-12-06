package com.swyp.mema.domain.voteDate.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FinalVoteDateReq {

	@NotNull(message = "최종 날짜는 필수입니다.")
	private LocalDate finalDate; // 최종 선택된 날짜
}
