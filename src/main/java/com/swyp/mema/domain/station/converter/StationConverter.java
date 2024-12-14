package com.swyp.mema.domain.station.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.swyp.mema.domain.station.dto.response.subwayInfo.SingleStationResponse;
import com.swyp.mema.domain.station.dto.response.subwayTime.SubwayTimeBasicResponse;
import com.swyp.mema.domain.station.dto.response.subwayTime.TotalSubwayTimeResponse;
import com.swyp.mema.domain.station.dto.response.subwayTime.SubwayTimeResponse;
import com.swyp.mema.domain.station.dto.response.subwayInfo.TotalStationResponse;
import com.swyp.mema.domain.station.model.Station;

@Component
public class StationConverter {

	private static final int START_INDEX = 1;
	private static final int END_INDEX = 1200;

	public TotalStationResponse toTotalStationResponse(List<Station> stationList) {

		List<SingleStationResponse> stationResponse = stationList.stream()
			.map(station -> new SingleStationResponse(
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

	public TotalSubwayTimeResponse toSubwayTimeListResponse(SubwayTimeBasicResponse basicResponse) {

		List<SubwayTimeResponse> list = basicResponse.getResponse().getBody().getItems().getItemList().stream()
			.map(res -> SubwayTimeResponse.builder()
				.stationId(res.getStationId())
				.stationName(res.getStationName())
				.routeId(res.getRouteId())
				.dailyTypeCode(res.getDailyTypeCode())
				.departureTime(res.getDepartureTime())
				.arrivalTime(res.getArrivalTime())
				.endStationId(res.getEndStationId())
				.endStationName(res.getEndStationName())
				.upDownTypeCode(res.getUpDownTypeCode())
				.build()
			).toList();

		return new TotalSubwayTimeResponse(list);
	}
}
