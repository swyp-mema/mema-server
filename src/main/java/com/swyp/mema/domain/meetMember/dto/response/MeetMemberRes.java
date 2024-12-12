package com.swyp.mema.domain.meetMember.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.swyp.mema.domain.user.dto.response.UserRes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({ "meetMemberId", "isMe", "userInfo" }) // 필드 순서 지정
public class MeetMemberRes {

	private Long meetMemberId;

	@JsonProperty("isMe") // JSON에서 "isMe"로 노출
	private boolean me;

	private UserRes userInfo;
}
