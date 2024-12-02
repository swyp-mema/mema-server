package com.swyp.mema.domain.meet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateMeetReq {

	@NotBlank(message = "약속명을 입력해주세요.") // 빈 문자열 또는 공백 방지
	@Size(min = 1, max = 20, message = "약속명은 1자 이상 20자 이하로 입력해주세요.") // 길이 제한
	private String meetName;    // 약속명
}