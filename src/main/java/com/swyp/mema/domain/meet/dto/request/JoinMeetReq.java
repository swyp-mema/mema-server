package com.swyp.mema.domain.meet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JoinMeetReq {
	private int joinCode; // 6자리 숫자 참여 코드

}
