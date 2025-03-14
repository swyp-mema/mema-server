package com.swyp.mema.database.openapi.location.converter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.swyp.mema.database.openapi.location.response.SubwayMasterBasicResponse;
import com.swyp.mema.database.openapi.location.response.SubwayMasterResponse;
import com.swyp.mema.database.openapi.location.response.TotalSubwayMasterResponse;

@Component
public class StationMasterConverter {

	public List<SubwayMasterResponse> toSubwayMasterListResponse(SubwayMasterBasicResponse basicResponse) {

		return basicResponse.getSubwayStationMaster().getRows().stream()
			.map(res -> SubwayMasterResponse.builder()
				.stationName(res.getBuildingName())
				.line(res.getRoute())
				.lat(res.getLatitude())
				.lot(res.getLongitude())
				.build()
			).toList();

	}

	public TotalSubwayMasterResponse toTotalSubwayMasterResponse(List<SubwayMasterResponse> responses) {
		return new TotalSubwayMasterResponse(responses);
	}
}
