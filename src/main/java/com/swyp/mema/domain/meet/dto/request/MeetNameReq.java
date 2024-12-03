package com.swyp.mema.domain.meet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "약속 생성 및 약속명 수정 요청")
public class MeetNameReq {

	@Schema(description = "약속명", example = "극 P들의 약속")
	@NotBlank(message = "약속명을 입력해주세요.") // 빈 문자열 또는 공백 방지
	@Size(min = 1, max = 20, message = "약속명은 1자 이상 20자 이하로 입력해주세요.") // 길이 제한
	private String meetName;    // 약속명
}