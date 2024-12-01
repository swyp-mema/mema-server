package com.swyp.mema.domain.meetMember.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meetMember.converter.MeetMemberConverter;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.user.dto.reseponse.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetMemberService {

	private final MeetMemberRepository meetMemberRepository;
	private final MeetMemberConverter meetMemberConverter;

	@Transactional
	public void addMeetMember(Meet meet, Long userId) {
		MeetMember meetMember = meetMemberConverter.toMeetMember(meet, userId);
		meetMemberRepository.save(meetMember);
	}

	@Transactional(readOnly = true)
	public List<UserResponse> getMeetMembers(Long meetId) {
		return meetMemberRepository.findMeetMembersWithUserInfo(meetId);
	}
}
