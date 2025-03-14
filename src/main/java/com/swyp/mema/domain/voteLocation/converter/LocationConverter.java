package com.swyp.mema.domain.voteLocation.converter;

import java.util.ArrayList;
import java.util.List;

import com.swyp.mema.database.station.dto.response.subwayInfo.SingleStationRes;
import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.domain.midlocation.dto.MidLocationDto;
import com.swyp.mema.domain.voteLocation.dto.response.MidLocationRes;
import com.swyp.mema.domain.voteLocation.dto.response.MidLocationTotalRes;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.swyp.mema.domain.voteLocation.dto.request.CreateLocationReq;
import com.swyp.mema.domain.voteLocation.dto.response.SingleLocationRes;
import com.swyp.mema.domain.voteLocation.model.Location;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.user.model.User;

@Component
public class LocationConverter {

	public Location toLocationEntity(CreateLocationReq request, Meet meet, User user) {

		return Location.builder()
			.meet(meet)
			.user(user)
			.stationName(request.getStationName())
			.stationRoute(request.getLineName())
			.lat(request.getLat())
			.lot(request.getLot())
			.build();
	}

	public SingleLocationRes toSingleLocationResponse(Location location) {
		return new SingleLocationRes(location.getStationName(), location.getLat(), location.getLot());
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
