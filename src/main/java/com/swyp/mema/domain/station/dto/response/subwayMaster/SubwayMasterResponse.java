package com.swyp.mema.domain.station.dto.response.subwayMaster;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubwayMasterResponse {

	// 역사명
	private String stationName;

	// 호선
	private String route;

	// 위도
	private String lat;

	// 경도
	private String lot;
}
