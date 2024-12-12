package com.swyp.mema.domain.voteLocation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.mema.domain.voteLocation.converter.LocationConverter;
import com.swyp.mema.domain.voteLocation.dto.request.CreateLocationReq;
import com.swyp.mema.domain.voteLocation.dto.response.SingleLocationResponse;
import com.swyp.mema.domain.voteLocation.dto.response.TotalLocationResponse;
import com.swyp.mema.domain.voteLocation.exception.LocationNotFoundException;
import com.swyp.mema.domain.voteLocation.model.Location;
import com.swyp.mema.domain.voteLocation.repository.LocationRepository;
import com.swyp.mema.domain.meet.exception.MeetNotFoundException;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.repository.MeetRepository;
import com.swyp.mema.domain.meetMember.exception.MeetMemberNotFoundException;
import com.swyp.mema.domain.meetMember.exception.NotMeetMemberException;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.user.exception.UserNotFoundException;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.repository.UserRepository;

@Service
public class LocationService {

	private final UserRepository userRepository;
	private final MeetRepository meetRepository;
	private final MeetMemberRepository meetMemberRepository;
	private final LocationRepository locationRepository;
	private final LocationConverter converter;


	public LocationService(UserRepository userRepository, MeetRepository meetRepository,
		MeetMemberRepository meetMemberRepository, LocationRepository locationRepository, LocationConverter converter) {
		this.userRepository = userRepository;
		this.meetRepository = meetRepository;
		this.meetMemberRepository = meetMemberRepository;
		this.locationRepository = locationRepository;
		this.converter = converter;
	}

	@Transactional
	public SingleLocationResponse saveLocation(CreateLocationReq createLocationReq, Long meetId, Long userId) {

		// 필수 검증 로직
		User user = validateUser(userId);
		Meet meet = validateMeet(meetId);
		MeetMember meetMember = validateMeetMember(user, meet);
		validateMeetMember(meetMember.getId());

		Location location = converter.toLocationEntity(createLocationReq, meet, user);

		locationRepository.save(location);

		return converter.toSingleLocationResponse(location);
	}

	@Transactional(readOnly = true)
	public SingleLocationResponse getMyLocation(Long meetId, Long userId) {

		// 필수 검증 로직
		User user = validateUser(userId);
		Meet meet = validateMeet(meetId);
		MeetMember meetMember = validateMeetMember(user, meet);
		validateMeetMember(meetMember.getId());

		Location location = locationRepository.findByUserAndMeet(user, meet)
			.orElseThrow(LocationNotFoundException::new);

		return converter.toSingleLocationResponse(location);
	}

	@Transactional(readOnly = true)
	public TotalLocationResponse getTotalLocation(Long meetId, Long userId) {

		// 필수 검증 로직
		User user = validateUser(userId);
		Meet meet = validateMeet(meetId);
		MeetMember meetMember = validateMeetMember(user, meet);
		validateMeetMember(meetMember.getId());

		List<Location> locations = locationRepository.findByMeetId(meetId);

		// 중간 위치 정하는 로직 여기 포함되어야할 듯

		return converter.toTotalLocationResponse(locations);
	}

	private MeetMember validateMeetMember(User user, Meet meet) {
		return meetMemberRepository.findByUserAndMeet(user, meet)
			.orElseThrow(NotMeetMemberException::new);
	}

	private MeetMember validateMeetMember(Long meetMemberId) {
		return meetMemberRepository.findById(meetMemberId)
			.orElseThrow(MeetMemberNotFoundException::new);
	}

	private Meet validateMeet(Long meetId) {
		return meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);
	}

	private User validateUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
	}
}
