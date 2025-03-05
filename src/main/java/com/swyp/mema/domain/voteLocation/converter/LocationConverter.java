package com.swyp.mema.domain.voteLocation.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.domain.midlocation.dto.MidLocationDto;
import com.swyp.mema.domain.station.dto.response.subwayInfo.SingleStationRes;
import com.swyp.mema.domain.voteLocation.dto.response.MidLocationRes;
import com.swyp.mema.domain.voteLocation.dto.response.MidLocationTotalRes;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.swyp.mema.domain.voteLocation.dto.request.CreateLocationReq;
import com.swyp.mema.domain.voteLocation.dto.response.SingleLocationRes;
import com.swyp.mema.domain.voteLocation.dto.response.TotalLocationRes;
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

	public SingleLocationRes toSingleLocationResponse(Location location) {
		return new SingleLocationRes(location.getStationName(), location.getLat(), location.getLot());
	}

	public TotalLocationRes toTotalLocationResponse(List<Location> locationList) {

		// Location 객체에서 필요한 필드를 추출 (예: stationName)
		List<String> startStationList = locationList.stream()
			.map(Location::getStationName) // Location의 stationName 필드 추출
			.collect(Collectors.toList());

		return TotalLocationRes.builder()
			.startStationList(startStationList)
			.arrivalStation("구현 중! 기다려주세용^_^")
			.build();
	}


	public SingleStationRes toSingleStationResponse(_Station station) {

		return SingleStationRes.builder()
				.lineName(station.getLineName())
				.stationName(station.getStationName())
				.lat(station.getLat())
				.lot(station.getLot())
				.build();
	}

	public MidLocationRes toMidLocationResponse(MidLocationDto dto) {

		List<SingleStationRes> stationPath = new ArrayList<>();
		for (_Station station : dto.getPath()) {

			stationPath.add(toSingleStationResponse(station));
		}

		return MidLocationRes.builder()
				.userId(dto.getUser().getUserId())
				.nickname(dto.getUser().getNickname())
				.puzId(dto.getUser().getPuzId())
				.puzColor(dto.getUser().getPuzColor())
				.role(dto.getUser().getRole())
				.stationPath(stationPath)
				.time(dto.getTime())
				.stationName(dto.getFirstStation().getStationName())
				.stationRoute(dto.getFirstStation().getLineName())
				.build();
	}

	public MidLocationTotalRes toMidLocationTotalResponse(Pair<_Station, List<MidLocationDto>> dtos) {

		MidLocationTotalRes midLocationTotalRes = MidLocationTotalRes.builder()
				.midStation(SingleStationRes.builder()
						.lineName(dtos.getFirst().getLineName())
						.stationName(dtos.getFirst().getStationName())
						.lat(dtos.getFirst().getLat())
						.lot(dtos.getFirst().getLot())
						.build())
				.users(new ArrayList<>())
				.build();

		for(MidLocationDto dto : dtos.getSecond()) {

			midLocationTotalRes.getUsers().add(
					toMidLocationResponse(dto));
		}

		return midLocationTotalRes;
	}
}
