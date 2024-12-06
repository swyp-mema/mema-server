package com.swyp.mema.domain.meetMember.converter;

import org.springframework.stereotype.Component;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.user.model.User;

@Component
public class MeetMemberConverter {

	public MeetMember toMeetMember(Meet meet, User user) {
		return MeetMember.builder()
			.meet(meet)
			.user(user)
			.voteDateYn(false) // 초기값 false
			.voteLocationYn(false) // 초기값 false
			.build();
	}
}
