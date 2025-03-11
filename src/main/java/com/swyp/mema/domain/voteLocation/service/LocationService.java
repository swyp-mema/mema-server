package com.swyp.mema.domain.voteLocation.service;

import java.util.List;

import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.domain.midlocation.dto.MidLocationDto;
import com.swyp.mema.domain.midlocation.service.MidLocationService;
import com.swyp.mema.domain.store.dto.naverMap.StoreInfoRes;
import com.swyp.mema.domain.store.dto.naverMap.TotalStoreInfoRes;
import com.swyp.mema.domain.store.exception.NotRecommendStore;
import com.swyp.mema.domain.store.service.naverMap.StoreServiceWithNaverMap;
import com.swyp.mema.domain.voteLocation.dto.response.MidLocationTotalRes;
import com.swyp.mema.domain.voteLocation.exception.MidLocationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.mema.domain.meet.model.vo.State;
import com.swyp.mema.domain.voteLocation.converter.LocationConverter;
import com.swyp.mema.domain.voteLocation.dto.request.CreateLocationReq;
import com.swyp.mema.domain.voteLocation.dto.response.SingleLocationRes;
import com.swyp.mema.domain.voteLocation.exception.DuplicateLocationVoteException;
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
@RequiredArgsConstructor
public class LocationService {

	private final UserRepository userRepository;
	private final MeetRepository meetRepository;
	private final MeetMemberRepository meetMemberRepository;
	private final LocationRepository locationRepository;
	private final LocationConverter converter;
	private final StoreServiceWithNaverMap storeService;
	private final MidLocationService midLocationService;
	private final LocationConverter locationConverter;


	@Transactional
	public SingleLocationRes saveLocation(CreateLocationReq createLocationReq, Long meetId, Long userId) {

		// 필수 검증 로직
		User user = validateUser(userId);
		Meet meet = validateMeet(meetId);
		MeetMember meetMember = validateMeetMember(user, meet);

		// 투표가 이미 존재하는지 검증
		if (locationRepository.findByUserAndMeet(user, meet).isPresent()) {
			throw new DuplicateLocationVoteException();
		}

		Location location = converter.toLocationEntity(createLocationReq, meet, user);
		locationRepository.save(location);

		meet.changeState(State.LOCATION_VOTING);
		meetMember.setVoteLocationYn(true);

		return converter.toSingleLocationResponse(location);
	}

	@Transactional(readOnly = true)
	public SingleLocationRes getMyLocation(Long meetId, Long userId) {

		// 필수 검증 로직
		User user = validateUser(userId);
		Meet meet = validateMeet(meetId);
		MeetMember meetMember = validateMeetMember(user, meet);
		validateMeetMember(meetMember.getId());

		Location location = locationRepository.findByUserAndMeet(user, meet)
			.orElseThrow(LocationNotFoundException::new);

		return converter.toSingleLocationResponse(location);
	}

	@Transactional
	public MidLocationTotalRes getTotalLocation(Long meetId, Long userId) {

		// 필수 검증 로직
		User user = validateUser(userId);
		Meet meet = validateMeet(meetId);
		MeetMember meetMember = validateMeetMember(user, meet);

		// 약속 ID와 약속원이 일치하는지 확인
		if (!meetMember.getMeet().getId().equals(meetId)) {
			throw new NotMeetMemberException();
		}

		// 유저들의 출발위치 responses
		List<Location> locations = locationRepository.findByMeetId(meetId);

		// 유저들의 출발 위치가 없는 경우 예외
		if (locations.isEmpty()) { throw new MidLocationNotFoundException(); }

		Pair<_Station, List<MidLocationDto>> totalMidStation = midLocationService.getTotalMidStation(locations);

		meet.setMeetLocation(totalMidStation.getFirst().getStationName(), totalMidStation.getFirst().getLineName(),
				totalMidStation.getFirst().getLat(), totalMidStation.getFirst().getLot());

		return locationConverter.toMidLocationTotalResponse(totalMidStation);
	}

	@Transactional(readOnly = true)
	public TotalStoreInfoRes recommendStore(Long userId, Long meetId) {

		// 필수 검증 로직
		User user = validateUser(userId);
		Meet meet = validateMeet(meetId);
		validateMeetMember(user, meet);

		// 만남 장소가 없는 경우 가게 추천 못하는 예외
		if (meet.getMeetLocation() == null) {
			throw new NotRecommendStore();
		}
		List<StoreInfoRes> storeInfos = storeService.getStoreInfo(meet.getMeetLocation());
		return new TotalStoreInfoRes(storeInfos);
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
