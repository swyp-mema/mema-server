package com.swyp.mema.domain.meetMember.repository;

import java.util.List;

import com.swyp.mema.domain.user.dto.reseponse.UserResponse;

public interface MeetMemberCustomRepository {
	List<UserResponse> findMeetMembersWithUserInfo(Long meetId); // 약속원과 사용자 정보 조회
}
