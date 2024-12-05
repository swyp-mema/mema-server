package com.swyp.mema.domain.meetMember.repository;

import java.util.List;

import com.swyp.mema.domain.meetMember.dto.response.MeetMemberRes;

public interface MeetMemberCustomRepository {
	List<MeetMemberRes> findMeetMembersWithUserInfo(Long meetId); // 약속원과 사용자 정보 조회
}
