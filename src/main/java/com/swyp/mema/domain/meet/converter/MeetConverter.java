package com.swyp.mema.domain.meet.converter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.swyp.mema.domain.meet.dto.request.MeetNameReq;
import com.swyp.mema.domain.meet.dto.response.CreateMeetRes;
import com.swyp.mema.domain.meet.dto.response.SingleMeetRes;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.model.vo.State;
import com.swyp.mema.domain.meetMember.dto.response.MeetMemberRes;

@Component
public class MeetConverter {

	public Meet toMeet(MeetNameReq meetReq, int code) {
		return Meet.builder()
			.code(code)
			.name(meetReq.getMeetName())
			.state(State.READY_DATE_VOTE)	// 초기값 : 날짜 투표 중
			.meetDate(null)
			.location(null)
			.expiredVoteDate(null)
			.expiredVoteLocation(null)
			.build();
	}

	public CreateMeetRes toCreateMeetResponse(Meet meet) {
		return CreateMeetRes.builder()
			.meetId(meet.getId())
			.meetCode(meet.getCode())
			.build();
	}

	public SingleMeetRes toMeetSingleResponse(Meet meet, List<MeetMemberRes> members) {
		return SingleMeetRes.builder()
			.meetId(meet.getId())
			.meetName(meet.getName())
			.meetState(meet.getState())
			.meetDate(meet.getMeetDate())
			.meetLocation(meet.getLocation())
			.voteExpiredDate(meet.getExpiredVoteDate())
			.voteExpiredLocation(meet.getExpiredVoteLocation())
			.members(members)
			.build();
	}
}
