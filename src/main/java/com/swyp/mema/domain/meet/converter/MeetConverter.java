package com.swyp.mema.domain.meet.converter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.swyp.mema.domain.meet.dto.request.CreateMeetReq;
import com.swyp.mema.domain.meet.dto.response.CreateMeetResponse;
import com.swyp.mema.domain.meet.dto.response.MeetSingleResponse;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.model.vo.State;
import com.swyp.mema.domain.user.dto.reseponse.UserResponse;

@Component
public class MeetConverter {

	public Meet toMeet(CreateMeetReq meetReq, int code) {
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

	public CreateMeetResponse toCreateMeetResponse(Meet meet) {
		return CreateMeetResponse.builder()
			.meetId(meet.getId())
			.meetCode(meet.getCode())
			.build();
	}

	public MeetSingleResponse toMeetSingleResponse(Meet meet, List<UserResponse> members) {
		return MeetSingleResponse.builder()
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
