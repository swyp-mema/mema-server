package com.swyp.mema.domain.locaction.dto.response;

import lombok.Getter;

@Getter
public class StationResponse {

	private String stationId;

	private String stationName;

	private String routeName;

	public StationResponse(String stationId, String stationName, String routeName) {
		this.stationId = stationId;
		this.stationName = stationName;
		this.routeName = routeName;
	}
}
