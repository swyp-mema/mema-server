package com.swyp.mema.domain.meetMember.dto.response;

import com.swyp.mema.domain.user.dto.reseponse.UserRes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetMemberRes {

	private Long meetMemberId;
	private UserRes userInfo;

}
