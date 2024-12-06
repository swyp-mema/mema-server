package com.swyp.mema.domain.voteDate.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.mema.domain.meet.exception.MeetNotFoundException;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.repository.MeetRepository;
import com.swyp.mema.domain.meetMember.dto.response.MeetMemberNameRes;
import com.swyp.mema.domain.meetMember.exception.MeetMemberNotFoundException;
import com.swyp.mema.domain.meetMember.exception.NotMeetMemberException;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.voteDate.dto.request.CreateVoteDateReq;
import com.swyp.mema.domain.voteDate.dto.request.FinalVoteDateReq;
import com.swyp.mema.domain.voteDate.dto.request.UpdateVoteDateReq;
import com.swyp.mema.domain.voteDate.dto.response.TotalVoteDateListRes;
import com.swyp.mema.domain.voteDate.dto.response.TotalVoteDateRes;
import com.swyp.mema.domain.voteDate.dto.response.VoteDateRes;
import com.swyp.mema.domain.voteDate.exception.VoteDateExpiredException;
import com.swyp.mema.domain.voteDate.model.VoteDate;
import com.swyp.mema.domain.voteDate.repository.VoteDateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoteDateService {

	private final MeetRepository meetRepository;
	private final MeetMemberRepository meetMemberRepository;
	private final VoteDateRepository voteDateRepository;

	@Transactional
	public TotalVoteDateListRes create(Long meetId, CreateVoteDateReq createVoteDateReq) {

		// 약속 조회
		Meet meet = meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);

		// 투표 만료일 검증
		validateVoteDateNotExpired(meet);
		
		// 약속원 조회
		MeetMember meetMember = meetMemberRepository.findById(createVoteDateReq.getMeetMemberId())
			.orElseThrow(MeetMemberNotFoundException::new);

		// 약속 ID 확인
		if (!meetMember.getMeet().getId().equals(meetId)) {
			throw new NotMeetMemberException();
		}

		// expiredVoteDate Null 이 아니면 새로 생성하는 날짜 투표임으로 만료일 저장
		if (createVoteDateReq.getExpiredVoteDate() != null) {
			meet.setExpiredVoteDate(createVoteDateReq.getExpiredVoteDate());
		}

		// 기존 투표 삭제
		voteDateRepository.deleteAllByMeetMember(meetMember);

		// 새로운 날짜 투표 저장
		List<VoteDate> voteDates = createVoteDateReq.getVoteDates().stream()
			.map(date -> VoteDate.builder()
				.meetMember(meetMember)
				.user(meetMember.getUser())
				.date(date)
				.build())
			.collect(Collectors.toList());

		// 날짜 데이터 한 번에 저장
		voteDateRepository.saveAll(voteDates);

		// 약속에 대한 전체 날짜 투표 반환
		return getVoteDatesByMeetId(meetId);
	}

	@Transactional(readOnly = true)
	public TotalVoteDateListRes getVoteDatesByMeetId(Long meetId) {

		// 1. 해당 meetId에 속한 모든 투표 데이터를 가져옴
		List<VoteDateRes> allByMeetId = voteDateRepository.findAllByMeetId(meetId);

		// 2. 날짜별로 그룹화 및 사용자 매핑
		Map<LocalDate, List<MeetMemberNameRes>> usersByDate = allByMeetId.stream()
			.collect(Collectors.groupingBy(
				VoteDateRes::getDate, // 날짜 기준으로 그룹화
				Collectors.mapping(
					voteDate -> new MeetMemberNameRes(
						voteDate.getMeetMemberId(),
						voteDate.getMeetMemberName()
					),
					Collectors.toList()
				)
			));

		// 3. 그룹화된 데이터를 날짜 기준으로 오름차순 정렬 후 TotalVoteDateRes로 변환
		List<TotalVoteDateRes> voteDateResponses = usersByDate.entrySet().stream()
			.sorted(Map.Entry.comparingByKey()) // 날짜 기준 오름차순 정렬
			.map(entry -> new TotalVoteDateRes(entry.getKey(), entry.getValue()))
			.collect(Collectors.toList());

		// 4. TotalVoteDateListRes 로 반환
		return new TotalVoteDateListRes(voteDateResponses);
	}

	// /**
	//  * 특정 약속원의 날짜 투표 조회
	//  * @param meetMemberId 약속원 ID
	//  * @return 해당 약속원의 날짜 투표 리스트
	//  */
	// @Transactional(readOnly = true)
	// public SingleVoteDateRes getVoteDatesByMeetMemberId(Long meetId) {
	//
	// 	// 1. 약속원의 날짜 투표 데이터 조회
	// 	List<VoteDate> voteDates = voteDateRepository.findAllByMeetMemberId(meetMemberId);
	//
	// 	// 2. 약속원의 이름과 날짜 리스트 추출
	// 	if (voteDates.isEmpty()) {
	// 		throw new VoteDateByMemberNotFoundException();
	// 	}
	//
	// 	// 약속원 정보 가져오기
	// 	MeetMember meetMember = voteDates.get(0).getMeetMember();
	//
	// 	// 날짜 리스트 추출
	// 	List<LocalDate> dates = voteDates.stream()
	// 		.map(VoteDate::getDate)
	// 		.collect(Collectors.toList());
	//
	// 	// 3. DTO 생성 및 반환
	// 	return new SingleVoteDateRes(
	// 		meetMember.getId(),
	// 		meetMember.getUser().getNickname(), // 약속원의 사용자 이름
	// 		dates
	// 	);
	// }

	/**
	 * 날짜 투표 수정
	 * @param meetId 약속 ID
	 * @param updateVoteDateReq 날짜 투표 요청 DTO
	 */
	@Transactional
	public TotalVoteDateListRes updateVoteDates(Long meetId, UpdateVoteDateReq updateVoteDateReq) {

		// 약속 조회
		Meet meet = meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);

		// 투표 만료일 검증
		validateVoteDateNotExpired(meet);
		
		// 약속원 조회
		MeetMember meetMember = meetMemberRepository.findById(updateVoteDateReq.getMeetMemberId())
			.orElseThrow(MeetMemberNotFoundException::new);

		// 약속 ID와 약속원이 일치하는지 확인
		if (!meetMember.getMeet().getId().equals(meetId)) {
			throw new NotMeetMemberException();
		}

		// 기존 투표 삭제
		voteDateRepository.deleteAllByMeetMember(meetMember);

		// 새로운 투표 데이터 저장
		List<VoteDate> newVoteDates = updateVoteDateReq.getVoteDates().stream()
			.map(date -> VoteDate.builder()
				.meetMember(meetMember)
				.user(meetMember.getUser())
				.date(date)
				.build())
			.collect(Collectors.toList());

		voteDateRepository.saveAll(newVoteDates);

		// 약속에 대한 전체 날짜 투표 반환
		return getVoteDatesByMeetId(meetId);
	}

	/**
	 * 최종 날짜 설정
	 * @param meetId 약속 ID
	 * @param finalVoteDateReq 최종 날짜 요청 DTO
	 */
	@Transactional
	public void setFinalVoteDate(Long meetId, FinalVoteDateReq finalVoteDateReq) {

		// 약속 조회
		Meet meet = meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);

		// 최종 날짜 설정
		meet.setMeetDate(finalVoteDateReq.getFinalDate());
	}

	/**
	 * 투표 만료일 검증 메서드
	 * @param meet 약속 객체
	 */
	private void validateVoteDateNotExpired(Meet meet) {
		if (meet.getExpiredVoteDate() != null && meet.getExpiredVoteDate().isBefore(LocalDateTime.now())) {
			throw new VoteDateExpiredException();
		}
	}
}
