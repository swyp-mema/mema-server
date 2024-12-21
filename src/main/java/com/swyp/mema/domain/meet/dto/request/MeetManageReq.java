package com.swyp.mema.domain.meet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetManageReq {

	private int offset; // 시작 위치
	private int limit;  // 한 번에 가져올 데이터 수

}
