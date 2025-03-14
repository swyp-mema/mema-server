package com.swyp.mema.domain.voteLocation.service;

import java.util.List;

import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.domain.midlocation.dto.MidLocationDto;
import com.swyp.mema.domain.midlocation.service.MidLocationService;
import com.swyp.mema.domain.store.dto.naverMap.StoreInfoRes;
import com.swyp.mema.domain.store.dto.naverMap.TotalStoreInfoRes;
import com.swyp.mema.domain.store.service.naverMap.StoreServiceWithNaverMap;
import com.swyp.mema.domain.voteLocation.dto.response.MidLocationTotalRes;
import com.swyp.mema.domain.voteLocation.exception.MidLocationNotFoundException;
import com.swyp.mema.global.validation.ValidationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.mema.domain.meet.model.vo.State;
import com.swyp.mema.domain.voteLocation.converter.LocationConverter;
import com.swyp.mema.domain.voteLocation.dto.request.CreateLocationReq;
import com.swyp.mema.domain.voteLocation.dto.response.SingleLocationRes;
import com.swyp.mema.domain.voteLocation.model.Location;
import com.swyp.mema.domain.voteLocation.repository.LocationRepository;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.user.model.User;

@Service
@RequiredArgsConstructor
public class LocationService {

	private final ValidationFacade validationFacade;
	private final LocationRepository locationRepository;
	private final LocationConverter locationConverter;

	private final StoreServiceWithNaverMap storeService;
	private final MidLocationService midLocationService;


	@Transactional
	public SingleLocationRes saveLocation(CreateLocationReq createLocationReq, Long meetId, Long userId) {

		// 필수 검증 로직
		User user = validationFacade.validateUserExists(userId);
		Meet meet = validationFacade.validateMeetExists(meetId);
		MeetMember meetMember = validationFacade.validateUserIsMeetMember(user, meet);

		// 사용자가 이미 위치 투표를 했는지 검증
		validationFacade.validateUserHasNotVoted(user, meet);

		Location location = locationConverter.toLocationEntity(createLocationReq, meet, user);
		locationRepository.save(location);

		// 약속 상태값 & 약속원 투표 상태값 변경
		meet.changeState(State.LOCATION_VOTING);
		meetMember.setVoteLocationYn(true);

		return locationConverter.toSingleLocationResponse(location);
	}

	@Transactional(readOnly = true)
	public SingleLocationRes getMyLocation(Long meetId, Long userId) {

		// 필수 검증 로직
		User user = validationFacade.validateUserExists(userId);
		Meet meet = validationFacade.validateMeetExists(meetId);
		MeetMember meetMember = validationFacade.validateUserIsMeetMember(user, meet);

		// 약속 ID와 약속원이 일치하는지 확인
		validationFacade.validateMeetMemberBelongToMeet(meetMember, meetId);

		// 사용자의 위치 투표 존재 여부 검증
		Location location = validationFacade.validateUserLocationExists(user, meet);
		return locationConverter.toSingleLocationResponse(location);
	}

	@Transactional
	public MidLocationTotalRes getTotalLocation(Long meetId, Long userId) {

		// 필수 검증 로직
		User user = validationFacade.validateUserExists(userId);
		Meet meet = validationFacade.validateMeetExists(meetId);
		MeetMember meetMember = validationFacade.validateUserIsMeetMember(user, meet);

		// 해당 약속의 약속원인지 검증
		validationFacade.validateMeetMemberBelongToMeet(meetMember, meetId);

		// 유저들의 출발 위치 responses
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
		User user = validationFacade.validateUserExists(userId);
		Meet meet = validationFacade.validateMeetExists(meetId);
		validationFacade.validateUserIsMeetMember(user, meet);

		// 만남 장소가 없는 경우 가게 추천 못하는 예외
		validationFacade.validateMeetHasLocation(meet);

		List<StoreInfoRes> storeInfos = storeService.getStoreInfo(meet.getMeetLocation());
		return new TotalStoreInfoRes(storeInfos);
	}
}
