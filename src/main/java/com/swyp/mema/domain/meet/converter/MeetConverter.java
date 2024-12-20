package com.swyp.mema.domain.meet.converter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.swyp.mema.domain.meet.dto.request.MeetNameReq;
import com.swyp.mema.domain.meet.dto.response.CreateMeetRes;
import com.swyp.mema.domain.meet.dto.response.MeetHomeDetailRes;
import com.swyp.mema.domain.meet.dto.response.SingleMeetRes;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.model.vo.State;
import com.swyp.mema.domain.meetMember.dto.response.MeetMemberRes;
import com.swyp.mema.domain.user.dto.response.UserRes;

@Component
public class MeetConverter {

	public Meet toMeet(MeetNameReq meetReq, int code) {
		return Meet.builder()
			.code(code)
			.name(meetReq.getMeetName())
			.state(State.CREATED)    // 초기값 : 날짜 투표 중
			.meetDate(null)
			.meetLocation(null)
			.expiredVoteDate(null)
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
			.joinCode(meet.getCode())
			.meetName(meet.getName())
			.meetState(meet.getState())
			.meetDate(meet.getMeetDate())
			.meetLocation(meet.getMeetLocation())
			.routeName(meet.getLine())
			.lat(meet.getLat())
			.lot(meet.getLot())
			.voteExpiredDate(meet.getExpiredVoteDate())
			.members(members)
			.build();
	}

	public MeetHomeDetailRes toMeetHomeDetailResponse(Meet meet, Long userId) {

		// MeetMemberRes
		List<MeetMemberRes> meetMemberRes = meet.getMembers().stream()
			.map(
				member -> {
					boolean result = member.getUser().getUserId() == userId;
					return new MeetMemberRes(
						member.getId(), // meetMemberId
						result,         // me
						new UserRes(    // userInfo
							member.getUser().getUserId(),   // userId
							member.getUser().getNickname(), // nickname
							member.getUser().getPuzId(),    // puzzleId
							member.getUser().getPuzColor()  // puzzleColor
						)
					);
				})
			.toList();

		return MeetHomeDetailRes.builder()
			.meetId(meet.getId())
			.joinCode(meet.getCode())
			.meetName(meet.getName())
			.meetDate(meet.getMeetDate())
			.memberCount(meet.getMembers().size())
			.members(meetMemberRes)
			.build();
	}
}
