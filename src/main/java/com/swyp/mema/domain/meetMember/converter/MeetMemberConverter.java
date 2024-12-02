package com.swyp.mema.domain.meetMember.converter;

import org.springframework.stereotype.Component;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meetMember.model.MeetMember;

@Component
public class MeetMemberConverter {

	public MeetMember toMeetMember(Meet meet, Long userId) {
		return MeetMember.builder()
			.meetId(meet.getId())
			.userId(userId)
			.voteDateYn(false) // 초기값 false
			.voteLocationYn(false) // 초기값 false
			.build();
	}
}
