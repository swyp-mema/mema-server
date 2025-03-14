package com.swyp.mema.domain.meet.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.swyp.mema.domain.badge.repository.BadgeRepository;
import com.swyp.mema.domain.meetMember.service.MeetMemberService;
import com.swyp.mema.global.validation.ValidationFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.mema.domain.meet.dto.request.JoinMeetReq;
import com.swyp.mema.domain.meet.dto.response.CreateMeetRes;
import com.swyp.mema.domain.meet.converter.MeetConverter;
import com.swyp.mema.domain.meet.dto.request.MeetNameReq;
import com.swyp.mema.domain.meet.dto.response.MeetHomeDetailRes;
import com.swyp.mema.domain.meet.dto.response.MeetHomeRes;
import com.swyp.mema.domain.meet.dto.response.SingleMeetRes;
import com.swyp.mema.domain.meet.dto.response.TotalMeetManageRes;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.model.vo.State;
import com.swyp.mema.domain.meet.repository.MeetRepository;
import com.swyp.mema.domain.meetMember.dto.response.MeetMemberRes;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.global.utils.RandomCodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetService {

	private final MeetMemberService meetMemberService;
	private final ValidationFacade validationFacade;
	private final MeetRepository meetRepository;
	private final MeetMemberRepository meetMemberRepository;
	private final BadgeRepository badgeRepository;
	private final MeetConverter meetConverter;

	/**
	 * 새로운 약속 생성 & 사용자는 약속원에 등록
	 */
	public CreateMeetRes create(MeetNameReq meetNameReq, Long userId) {

		// 존재하는 사용자인지 검증
		User user = validationFacade.validateUserExists(userId);

		// 진행 중인 약속은 최대 4개까지 생성되도록 검증
		validationFacade.validateUserCanCreateMoreMeets(userId);

		// 참여 코드 생성
		int code = generateUniqueMeetCode();

		// 새로운 약속 생성
		Meet meet = meetConverter.toMeet(meetNameReq, code);
		meetRepository.save(meet);

		// 생성된 약속에 약속원으로 등록
		MeetMember meetMember = meetMemberService.addMeetMember(meet, user);
		meet.addMember(meetMember);	// 객체 그래프 동기화

		// 뱃지 설정
		badgeRepository.findByUser(user).createMeet();

		return meetConverter.toCreateMeetResponse(meet);
	}

	/**
	 * 참여 코드를 통해 약속에 참여 & 약속원에 등록
	 */
	public SingleMeetRes join(JoinMeetReq joinMeetReq, Long userId) {

		// 존재하는 사용자인지 검증
		User user = validationFacade.validateUserExists(userId);

		// 참여코드로 존재하는 약속인지 검증
		Meet meet = validationFacade.validateMeetExistsByCode(joinMeetReq.getJoinCode());

		// 이미 등록된 약속원인지 확인
		validationFacade.validateUserNotAlreadyInMeet(meet, user);

		// 진행 중인 약속 개수 검증
		validationFacade.validateUserCanCreateMoreMeets(userId);

		// 약속원 등록
		MeetMember meetMember = meetMemberService.addMeetMember(meet, user);
		meet.addMember(meetMember);	// 객체 그래프 동기화

		return getSingle(meet.getId(), userId);
	}

	/**
	 * 약속 단건 조회
	 */
	@Transactional(readOnly = true)
	public SingleMeetRes getSingle(Long meetId, Long userId) {

		// 존재하는 사용자인지 검증
		User user = validationFacade.validateUserExists(userId);

		// 존재하는 약속인지 검증
		Meet meet = validationFacade.validateMeetExists(meetId);

		// 사용자가 해당 약속의 약속원인지 검증
		validationFacade.validateUserIsMeetMember(user, meet);

		// 해당 약속에 소속된 모든 약속원 조회
		List<MeetMemberRes> members = meetMemberRepository.findMeetMembersWithUserInfo(meetId, userId);

		return meetConverter.toMeetSingleResponse(meet, members);
	}

	/**
	 * 약속 관리
	 */
	public TotalMeetManageRes getAll(Long userId, int offset, int limit) {

		// 존재하는 사용자인지 검증
		validationFacade.validateUserExists(userId);

		// 사용자가 속한 모든 약속(meet) 가져오기 (페이징 적용)
		List<Meet> meets = meetRepository.findMeetsByUserId(userId, offset, limit);

		// hasNext 계산
		boolean hasNext = meets.size() > limit;

		// hasNext가 true면 limit까지만 데이터 반환
		List<Meet> pagedMeets = hasNext ? meets.subList(0, limit) : meets;

		// 가져온 약속을 MeetHomeDetailRes DTO 리스트로 변환
		List<MeetHomeDetailRes> meetList = pagedMeets.stream()
			.map(m -> meetConverter.toMeetHomeDetailResponse(m, userId))
			.collect(Collectors.toList());

		return TotalMeetManageRes.builder()
			.meetList(meetList)
			.hasNext(hasNext)
			.pageSize(meetList.size())
			.build();
	}

	/**
	 * 약속명 수정
	 */
	public SingleMeetRes update(Long meetId, MeetNameReq meetNameReq, Long userId) {

		// 존재하는 사용자인지 검증
		User user = validationFacade.validateUserExists(userId);

		// 아이디에 해당하는 약속 존재 유무 검증
		Meet meet = validationFacade.validateMeetExists(meetId);

		// 사용자가 해당 약속의 약속원인지 검증
		validationFacade.validateUserIsMeetMember(user, meet);

		// Dirty Checking(변경 감지)로 약속명 수정
		meet.changeName(meetNameReq.getMeetName());

		// 약속원 목록 조회
		List<MeetMemberRes> members = meetMemberRepository.findMeetMembersWithUserInfo(meetId, userId);

		return meetConverter.toMeetSingleResponse(meet, members);
	}

	/**
	 * 약속 삭제 (hard delete)
	 */
	public void delete(Long meetId, Long userId) {

		// 존재하는 사용자인지 검증
		User user = validationFacade.validateUserExists(userId);

		// 약속이 존재하는지 확인
		Meet meet = validationFacade.validateMeetExists(meetId);

		// 사용자가 해당 약속의 약속원인지 검증
		validationFacade.validateUserIsMeetMember(user, meet);

		// 약속 삭제시 모든 약속원들 데이터도 삭제된다.
		meetRepository.delete(meet);
	}

	public MeetHomeRes getHome(Long userId) {

		// 존재하는 사용자인지 검증
		User user = validationFacade.validateUserExists(userId);

		// 사용자의 모든 약속(Meet) 조회
		List<Meet> meets = meetMemberRepository.findMeetsByUserId(userId);

		// 사용자가 참여한 약속이 없다면 빈 리스트 반환
		if (meets.isEmpty()) {
			return new MeetHomeRes(Collections.emptyList(), Collections.emptyList());
		}

		// 현재 날짜
		LocalDate today = LocalDate.now();

		// dirty checking 통해 State 업데이트
		meets.forEach(meet -> {
			// 만남 일자가 지나버리면 COMPLETED 로 상태값 변경
			if (meet.getMeetDate() != null && meet.getMeetDate().isBefore(today) &&
				meet.getState() != State.SETTLING && meet.getState() != State.COMPLETED) {
				meet.changeState(State.COMPLETED);
			}
		});

		// 곧 만나요 (meetDate >= 오늘, 최신순 4개)
		// (State.CREATED, DATE_VOTING, LOCATION_VOTING, READY 포함)
		List<MeetHomeDetailRes> upcomingMeets = meets.stream()
			.filter(meet -> {
				// 약속 상태가 CREATED, DATE_VOTING, LOCATION_VOTING, READY
				return
					meet.getState() == State.CREATED ||
					meet.getState() == State.DATE_VOTING ||
					meet.getState() == State.LOCATION_VOTING ||
					meet.getState() == State.READY;
			})
			.sorted(Comparator
				.comparing((Meet meet) -> meet.getMeetDate() == null ? LocalDate.MAX : meet.getMeetDate()) // 날짜가 가까운 순서로 정렬
				.thenComparing(Meet::getCreateDate) // 생성 날짜가 빠른 순서
			)
			.limit(4) // 최대 4개로 제한
			.map(m -> meetConverter.toMeetHomeDetailResponse(m, userId)) // DTO 변환
			.collect(Collectors.toList());

		// 즐거웠어요 (meetDate < 오늘, 최신순 4개)
		// 즐거웠어요 (State.COMPLETED, SETTLING 포함)
		List<MeetHomeDetailRes> pastMeets = meets.stream()
			.filter(meet -> {
				// 약속 상태가 COMPLETED, SETTLING
				return
					meet.getState() == State.COMPLETED ||
					meet.getState() == State.SETTLING;
			})
			.sorted(Comparator
				.comparing(Meet::getMeetDate).reversed() // 날짜가 가까운 순서로 정렬(과거부터)
				.thenComparing(Meet::getCreateDate) // 생성 날짜가 빠른 순서
			)
			.limit(4) // 최대 4개로 제한
			.map(m -> meetConverter.toMeetHomeDetailResponse(m, userId)) // DTO 변환
			.collect(Collectors.toList());

		// 결과를 Response 객체로 반환
		return new MeetHomeRes(upcomingMeets, pastMeets);
	}


	/**
	 * 중복되지 않는 6글자 숫자 형식의 참여 코드 생성
	 */
	private int generateUniqueMeetCode() {

		int code;
		do {
			code = RandomCodeGenerator.generateCode(); // 난수 생성 호출
		} while (validationFacade.validateMeetCodeDuplicate(code)); // 중복 여부 검증을 Validator 에 위임
		return code;
	}
}
