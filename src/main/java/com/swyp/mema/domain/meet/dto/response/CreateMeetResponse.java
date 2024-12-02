package com.swyp.mema.domain.meet.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateMeetResponse {

	private Long meetId;
	private int meetCode;

	@Builder
	public CreateMeetResponse(Long meetId, int meetCode) {
		this.meetId = meetId;
		this.meetCode = meetCode;
	}
}
