package com.swyp.mema.domain.locaction.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.swyp.mema.domain.locaction.dto.request.CreateLocationReq;
import com.swyp.mema.domain.locaction.dto.response.LocationResponse;
import com.swyp.mema.domain.locaction.dto.response.OpenApiBasicResponse;
import com.swyp.mema.domain.locaction.dto.response.SingleLocationResponse;
import com.swyp.mema.domain.locaction.dto.response.StationResponse;
import com.swyp.mema.domain.locaction.dto.response.TotalLocationResponse;
import com.swyp.mema.domain.locaction.dto.response.TotalStationResponse;
import com.swyp.mema.domain.locaction.model.Location;
import com.swyp.mema.domain.locaction.model.Station;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.user.model.User;

@Component
public class LocationConverter {

	private static final int START_INDEX = 1;
	private static final int END_INDEX = 1200;

	// Open API 기본 반환 형식 DTO -> LocationResponse 변환
	public LocationResponse toLocationResponse(OpenApiBasicResponse response) {

		return LocationResponse.builder()
			.pageNo(response.getResponse().getBody().getPageNo())
			.numOfRows(response.getResponse().getBody().getNumOfRows())
			.totalCount(response.getResponse().getBody().getTotalCount())
			.subwayList(response.getResponse().getBody().getItems().getItemList())
			.build();

	}

	public TotalStationResponse toTotalStationResponse(List<Station> stationList) {

		List<StationResponse> stationResponse = stationList.stream()
			.map(station -> new StationResponse(
				station.getStationId(),
				station.getStationName(),
				station.getRouteName()
			))
			.collect(Collectors.toList());

		int totalCount = stationResponse.size();
		return TotalStationResponse.builder()
			.pageNo(START_INDEX)
			.numOfRows(END_INDEX)
			.totalCount(totalCount)
			.stationList(stationResponse)
			.build();
	}

	public Location toLocationEntity(CreateLocationReq request, Meet meet, User user) {

		return Location.builder()
			.meet(meet)
			.user(user)
			.stationId(request.getStationId())
			.stationName(request.getStationName())
			.stationRoute(request.getRouteName())
			.build();
	}

	public SingleLocationResponse toSingleLocationResponse(Location location) {
		return new SingleLocationResponse(location.getStationName());
	}

	public TotalLocationResponse toTotalLocationResponse(List<Location> locationList) {

		// Location 객체에서 필요한 필드를 추출 (예: stationName)
		List<String> startStationList = locationList.stream()
			.map(Location::getStationName) // Location의 stationName 필드 추출
			.collect(Collectors.toList());

		return TotalLocationResponse.builder()
			.startStationList(startStationList)
			.arrivalStation("구현 중! 기다려주세용^_^")
			.build();
	}

}
