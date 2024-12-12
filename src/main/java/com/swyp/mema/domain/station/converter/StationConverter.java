package com.swyp.mema.domain.station.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.swyp.mema.domain.station.dto.response.SingleStationResponse;
import com.swyp.mema.domain.station.dto.response.TotalStationResponse;
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
}
