package com.swyp.mema.domain.station.dto.response.subwayInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SingleStationRes {

	private String stationName;

	private String lineName;

	private String lat;	// 위도

	private String lot; // 경도

}
