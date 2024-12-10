package com.swyp.mema.domain.locaction.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.swyp.mema.domain.locaction.dto.response.LocationResponse;
import com.swyp.mema.domain.locaction.dto.response.OpenApiBasicResponse;
import com.swyp.mema.domain.locaction.dto.response.StationResponse;
import com.swyp.mema.domain.locaction.dto.response.TotalStationResponse;
import com.swyp.mema.domain.locaction.model.Station;

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
}
