package com.swyp.mema.domain.station.dto.response;

import lombok.Getter;

@Getter
public class SingleStationResponse {

	private String stationId;

	private String stationName;

	private String routeName;

	public SingleStationResponse(String stationId, String stationName, String routeName) {
		this.stationId = stationId;
		this.stationName = stationName;
		this.routeName = routeName;
	}
}
