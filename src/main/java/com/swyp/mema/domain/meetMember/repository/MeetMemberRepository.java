package com.swyp.mema.domain.meetMember.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.mema.domain.meetMember.model.MeetMember;

public interface MeetMemberRepository extends JpaRepository<MeetMember, Long>, MeetMemberCustomRepository {
}
