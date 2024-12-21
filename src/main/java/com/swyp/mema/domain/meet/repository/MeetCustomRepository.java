package com.swyp.mema.domain.meet.repository;

import java.util.List;

import com.swyp.mema.domain.meet.model.Meet;

public interface MeetCustomRepository {

	Long countActiveMeetsByUserId(Long userId);

	List<Meet> findMeetsByUserId(Long userId, int offset, int limit);
}
