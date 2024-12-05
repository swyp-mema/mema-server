package com.swyp.mema.domain.meetMember.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.user.model.User;

public interface MeetMemberRepository extends JpaRepository<MeetMember, Long>, MeetMemberCustomRepository {

	boolean existsByMeetAndUser(Meet meet, User user);

}
