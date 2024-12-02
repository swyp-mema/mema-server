package com.swyp.mema.domain.meet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.mema.domain.meet.model.Meet;

public interface MeetRepository extends JpaRepository<Meet, Long> {
	boolean existsByCode(int code);
}
