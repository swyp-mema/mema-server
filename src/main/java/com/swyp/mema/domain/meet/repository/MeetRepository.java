package com.swyp.mema.domain.meet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.mema.domain.meet.model.Meet;

public interface MeetRepository extends JpaRepository<Meet, Long> {

	//참여 코드가 중복인지 확인
	boolean existsByCode(int code);

	// 해당 참여 코드의 약속이 있는지 확인
	Optional<Meet> findByCode(int code);
}
