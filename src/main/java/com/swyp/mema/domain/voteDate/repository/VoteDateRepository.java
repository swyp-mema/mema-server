package com.swyp.mema.domain.voteDate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.voteDate.model.VoteDate;

public interface VoteDateRepository extends JpaRepository<VoteDate, Long>, VoteDateCustomRepository {

	void deleteAllByMeetMember(MeetMember meetMember);

	//특정 약속원 ID 기준으로 날짜 투표 조회
	List<VoteDate> findAllByMeetMemberId(Long meetMemberId);
}
