package com.swyp.mema.domain.meet.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetHomeResponse {

	private List<MeetHomeDetailResponse> upcomingMeets;	// 곧 만나요
	private List<MeetHomeDetailResponse> pastMeets;		// 즐거웠어요
}
