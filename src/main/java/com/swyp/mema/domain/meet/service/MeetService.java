package com.swyp.mema.domain.meet.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.mema.domain.meet.dto.request.JoinMeetReq;
import com.swyp.mema.domain.meet.dto.response.CreateMeetRes;
import com.swyp.mema.domain.meet.converter.MeetConverter;
import com.swyp.mema.domain.meet.dto.request.MeetNameReq;
import com.swyp.mema.domain.meet.dto.response.SingleMeetRes;
import com.swyp.mema.domain.meet.exception.JoinCodeInvalidException;
import com.swyp.mema.domain.meet.exception.MeetNotFoundException;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.repository.MeetRepository;
import com.swyp.mema.domain.meetMember.converter.MeetMemberConverter;
import com.swyp.mema.domain.meetMember.dto.response.MeetMemberRes;
import com.swyp.mema.domain.meetMember.exception.NotMeetMemberException;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.user.exception.UserAlreadyRegisteredException;
import com.swyp.mema.domain.user.exception.UserNotFoundException;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.repository.UserRepository;
import com.swyp.mema.global.utils.RandomCodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetService {

	private final MeetRepository meetRepository;
	private final MeetConverter meetConverter;
	private final MeetMemberRepository meetMemberRepository;
	private final MeetMemberConverter meetMemberConverter;
	private final UserRepository userRepository;

	/**
	 * 새로운 약속 생성 & 사용자는 약속원에 등록
	 */
	public CreateMeetRes create(MeetNameReq meetNameReq, Long userId) {

		// 존재하는 사용자인지 검증
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);

		// 참여 코드 생성
		int code = generateUniqueMeetCode();

		// 새로운 약속 생성
		Meet meet = meetConverter.toMeet(meetNameReq, code);
		meetRepository.save(meet);

		// 생성된 약속에 약속원으로 등록
		MeetMember meetMember = meetMemberConverter.toMeetMember(meet, user);
		meetMemberRepository.save(meetMember);

		return meetConverter.toCreateMeetResponse(meet);
	}

	/**
	 * 참여 코드를 통해 약속에 참여 & 약속원에 등록
	 */
	public SingleMeetRes join(JoinMeetReq joinMeetReq, Long userId) {

		// 존재하는 사용자인지 검증
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);

		// 참여코드로 존재하는 약속인지 검증
		Meet meet = meetRepository.findByCode(joinMeetReq.getJoinCode())
			.orElseThrow(JoinCodeInvalidException::new);

		// 이미 등록된 약속원인지 확인
		if (meetMemberRepository.existsByMeetAndUser(meet, user)) {
			throw new UserAlreadyRegisteredException();
		}

		// 약속원 등록
		MeetMember meetMember = meetMemberConverter.toMeetMember(meet, user);
		meetMemberRepository.save(meetMember);

		return getSingle(meet.getId(), userId);
	}

	/**
	 * 약속 단건 조회
	 */
	@Transactional(readOnly = true)
	public SingleMeetRes getSingle(Long meetId, Long userId) {

		// 존재하는 사용자인지 검증
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);

		// 존재하는 약속인지 검증
		Meet meet = meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);

		// 사용자가 해당 약속의 약속원인지 검증
		meetMemberRepository.findByUserAndMeet(user, meet)
			.orElseThrow(NotMeetMemberException::new);

		// 해당 약속에 소속된 모든 약속원 조회
		List<MeetMemberRes> members = meetMemberRepository.findMeetMembersWithUserInfo(meetId);

		return meetConverter.toMeetSingleResponse(meet, members);

	}

	/**
	 * 약속명 수정
	 */
	public SingleMeetRes update(Long meetId, MeetNameReq meetNameReq, Long userId) {

		// 존재하는 사용자인지 검증
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);

		// 아이디에 해당하는 약속 존재 유무 검증
		Meet meet = meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);

		// 사용자가 해당 약속의 약속원인지 검증
		meetMemberRepository.findByUserAndMeet(user, meet)
			.orElseThrow(NotMeetMemberException::new);

		// Dirty Checking(변경 감지)로 약속명 수정
		meet.changeName(meetNameReq.getMeetName());

		// 약속원 목록 조회
		List<MeetMemberRes> members = meetMemberRepository.findMeetMembersWithUserInfo(meetId);

		return meetConverter.toMeetSingleResponse(meet, members);
	}

	/**
	 * 약속 삭제 (hard delete)
	 */
	public void delete(Long meetId, Long userId) {

		// 존재하는 사용자인지 검증
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);

		// 약속이 존재하는지 확인
		Meet meet = meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);

		// 사용자가 해당 약속의 약속원인지 검증
		meetMemberRepository.findByUserAndMeet(user, meet)
			.orElseThrow(NotMeetMemberException::new);

		// 약속 삭제시 모든 약속원들 데이터도 삭제된다.
		meetRepository.delete(meet);
	}

	/**
	 * 중복되지 않는 6글자 숫자 형식의 참여 코드 생성
	 */
	private int generateUniqueMeetCode() {

		int code;
		do {
			code = RandomCodeGenerator.generateCode(); // 난수 생성 호출
		} while (meetRepository.existsByCode(code)); // 중복 확인
		return code;
	}
}
