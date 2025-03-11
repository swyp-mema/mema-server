package com.swyp.mema.domain.meetMember.service;

import com.swyp.mema.global.validation.ValidationFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meetMember.converter.MeetMemberConverter;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetMemberService {

	private final ValidationFacade validationFacade;
	private final MeetMemberRepository meetMemberRepository;
	private final MeetMemberConverter meetMemberConverter;

	@Transactional
	public MeetMember addMeetMember(Meet meet, User user) {

		// 이미 등록된 약속원인지 확인
		validationFacade.validateUserNotAlreadyInMeet(meet, user);

		// 약속원 등록
		MeetMember meetMember = meetMemberConverter.toMeetMember(meet, user);
		return meetMemberRepository.save(meetMember);
	}
}
