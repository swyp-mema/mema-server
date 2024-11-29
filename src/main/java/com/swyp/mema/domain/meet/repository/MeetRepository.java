package com.swyp.mema.domain.meet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swyp.mema.domain.meet.model.Meet;

@Repository
public interface MeetRepository extends JpaRepository<Meet, Long> {
}
