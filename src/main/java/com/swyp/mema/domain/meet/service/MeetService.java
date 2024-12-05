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
import com.swyp.mema.domain.meetMember.dto.response.MeetMemberRes;
import com.swyp.mema.domain.meetMember.service.MeetMemberService;
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
	private final UserRepository userRepository;
	private final MeetConverter meetConverter;
	private final MeetMemberService meetMemberService;

	/**
	 * 새로운 약속 생성 & 사용자는 약속원에 등록
	 * @param meetNameReq
	 * @return meetId
	 */
	public CreateMeetRes create(MeetNameReq meetNameReq, Long userId) {

		// 참여 코드 생성
		int code = generateUniqueMeetCode();

		Meet meet = meetConverter.toMeet(meetNameReq, code);
		meetRepository.save(meet);

		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);

		meetMemberService.addMeetMember(meet, user);

		return meetConverter.toCreateMeetResponse(meet);
	}

	/**
	 * 참여 코드를 통해 약속에 참여 & 약속원에 등록
	 */
	public SingleMeetRes joinMeet(JoinMeetReq joinMeetReq, Long userId) {

		// 약속 조회
		Meet meet = meetRepository.findByCode(joinMeetReq.getJoinCode())
			.orElseThrow(JoinCodeInvalidException::new);

		// 사용자 조회
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);

		// 약속원 등록
		meetMemberService.addMeetMember(meet, user);

		return getSingle(meet.getId());
	}

	/**
	 * 약속 단건 조회
	 * @param meetId
	 * @return MeetSingleRes
	 */
	@Transactional(readOnly = true)
	public SingleMeetRes getSingle(Long meetId) {

		// 아이디에 해당하는 약속 존재 유무 검증
		Meet meet = meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);

		// 약속원 목록 조회
		List<MeetMemberRes> members = meetMemberService.getMeetMembers(meetId);

		return meetConverter.toMeetSingleResponse(meet, members);

	}

	/**
	 * 약속명 수정
	 * @param meetId
	 * @param meetNameReq
	 * @return MeetSingleRes
	 */
	public SingleMeetRes update(Long meetId, MeetNameReq meetNameReq) {

		// 아이디에 해당하는 약속 존재 유무 검증
		Meet meet = meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);

		// Dirty Checking(변경 감지)로 약속명 수정
		meet.changeName(meetNameReq.getMeetName());

		// 약속원 목록 조회
		List<MeetMemberRes> members = meetMemberService.getMeetMembers(meetId);

		return meetConverter.toMeetSingleResponse(meet, members);
	}

	/**
	 * 약속 삭제 (hard delete)
	 * @param meetId
	 */
	public void delete(Long meetId) {

		// 약속이 존재하는지 확인
		Meet meet = meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);

		// 약속 삭제시 해당 약속의 약속원들도 삭제된다.
		meetRepository.delete(meet);
	}

	/**
	 * 중복되지 않는 6글자 숫자 형식의 참여 코드 생성
	 * @return code
	 */
	private int generateUniqueMeetCode() {

		int code;
		do {
			code = RandomCodeGenerator.generateCode(); // 난수 생성 호출
		} while (meetRepository.existsByCode(code)); // 중복 확인
		return code;
	}
}
