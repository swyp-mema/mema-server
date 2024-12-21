package com.swyp.mema.domain.station.dto.response.subwayInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SingleStationResponse {

	private String stationName;

	private String routeName;

	private String lat;	// 위도

	private String lot; // 경도

}
