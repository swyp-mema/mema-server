package com.swyp.mema.domain.locaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.mema.domain.locaction.model.Location;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.user.model.User;

public interface LocationRepository extends JpaRepository<Location, Long> {

	Optional<Location> findByUserAndMeet(User user, Meet meet);
	List<Location> findByMeetId(Long meetId);
}
