package com.swyp.mema.domain.voteLocation.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.swyp.mema.domain.voteLocation.dto.request.CreateLocationReq;
import com.swyp.mema.domain.voteLocation.dto.response.SingleLocationResponse;
import com.swyp.mema.domain.voteLocation.dto.response.TotalLocationResponse;
import com.swyp.mema.domain.voteLocation.model.Location;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.user.model.User;

@Component
public class LocationConverter {

	// // Open API 기본 반환 형식 DTO -> LocationResponse 변환
	// public LocationResponse toLocationResponse(OpenApiBasicResponse response) {
	//
	// 	return LocationResponse.builder()
	// 		.pageNo(response.getResponse().getBody().getPageNo())
	// 		.numOfRows(response.getResponse().getBody().getNumOfRows())
	// 		.totalCount(response.getResponse().getBody().getTotalCount())
	// 		.subwayList(response.getResponse().getBody().getItems().getItemList())
	// 		.build();
	//
	// }

	public Location toLocationEntity(CreateLocationReq request, Meet meet, User user) {

		return Location.builder()
			.meet(meet)
			.user(user)
			.stationName(request.getStationName())
			.stationRoute(request.getRouteName())
			.lat(request.getLat())
			.lot(request.getLot())
			.build();
	}

	public SingleLocationResponse toSingleLocationResponse(Location location) {
		return new SingleLocationResponse(location.getStationName(), location.getLat(), location.getLot());
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
