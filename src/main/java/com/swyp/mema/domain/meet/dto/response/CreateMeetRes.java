package com.swyp.mema.domain.meet.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "약속 생성 응답")
public class CreateMeetRes {

	@Schema(description = "생성된 약속 ID", example = "1")
	private Long meetId;

	@Schema(description = "약속 참여 코드", example = "123123")
	private int meetCode;

	@Builder
	public CreateMeetRes(Long meetId, int meetCode) {
		this.meetId = meetId;
		this.meetCode = meetCode;
	}
}
