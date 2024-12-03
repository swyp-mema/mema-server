package com.swyp.mema.domain.meet.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.mema.domain.meet.dto.response.CreateMeetRes;
import com.swyp.mema.domain.meet.converter.MeetConverter;
import com.swyp.mema.domain.meet.dto.request.MeetNameReq;
import com.swyp.mema.domain.meet.dto.response.MeetSingleRes;
import com.swyp.mema.domain.meet.exception.MeetNotFoundException;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.repository.MeetRepository;
import com.swyp.mema.domain.meetMember.service.MeetMemberService;
import com.swyp.mema.domain.user.dto.reseponse.UserRes;
import com.swyp.mema.global.utils.RandomCodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetService {

	private final MeetRepository meetRepository;
	private final MeetConverter meetConverter;
	private final MeetMemberService meetMemberService;

	/**
	 * 새로운 약속 생성
	 * @param meetNameReq
	 * @return meetId
	 */
	@Transactional
	public CreateMeetRes create(MeetNameReq meetNameReq) {

		// 참여 코드 생성
		int code = generateUniqueMeetCode();

		Meet meet = meetConverter.toMeet(meetNameReq, code);
		meetRepository.save(meet);

		// 약속원 생성 위임
		Long userId = 1L; // 임시 값
		meetMemberService.addMeetMember(meet, userId);

		return meetConverter.toCreateMeetResponse(meet);
	}

	@Transactional(readOnly = true)
	public MeetSingleRes getSingle(Long meetId) {

		// 아이디에 해당하는 약속 존재 유무 검증
		Meet meet = meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);

		// 약속원 목록 조회
		List<UserRes> members = meetMemberService.getMeetMembers(meetId);

		return meetConverter.toMeetSingleResponse(meet, members);

	}

	@Transactional
	public MeetSingleRes update(Long meetId, MeetNameReq meetNameReq) {

		// 아이디에 해당하는 약속 존재 유무 검증
		Meet meet = meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);

		// Dirty Checking(변경 감지)로 약속명 수정
		meet.changeName(meetNameReq.getMeetName());

		// 약속원 목록 조회
		List<UserRes> members = meetMemberService.getMeetMembers(meetId);

		return meetConverter.toMeetSingleResponse(meet, members);
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
