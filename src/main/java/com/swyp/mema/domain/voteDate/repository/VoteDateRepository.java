package com.swyp.mema.domain.voteDate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.voteDate.model.VoteDate;

public interface VoteDateRepository extends JpaRepository<VoteDate, Long>, VoteDateCustomRepository {

	void deleteAllByMeetMember(MeetMember meetMember);

}
