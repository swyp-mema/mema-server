package com.swyp.mema.domain.meetMember.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meetMember.converter.MeetMemberConverter;
import com.swyp.mema.domain.meetMember.dto.response.MeetMemberRes;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.user.exception.UserAlreadyRegisteredException;
import com.swyp.mema.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetMemberService {

	private final MeetMemberRepository meetMemberRepository;
	private final MeetMemberConverter meetMemberConverter;

	@Transactional
	public void addMeetMember(Meet meet, User user) {

		// 이미 등록된 약속원인지 확인
		if (meetMemberRepository.existsByMeetAndUser(meet, user)) {
			throw new UserAlreadyRegisteredException();
		}

		// 약속원 등록
		MeetMember meetMember = meetMemberConverter.toMeetMember(meet, user);
		meetMemberRepository.save(meetMember);
	}

	@Transactional(readOnly = true)
	public List<MeetMemberRes> getMeetMembers(Long meetId) {
		return meetMemberRepository.findMeetMembersWithUserInfo(meetId);
	}
}
