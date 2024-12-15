package com.swyp.mema.domain.station.dto.response.subwayMaster;


import lombok.Builder;
import lombok.Getter;


/*
	위경도
 */
@Getter
@Builder
public class SubwayMasterResponse {

	// 역사명
	private String stationName;

	// 호선
	private String line;

	// 위도
	private String lat;

	// 경도
	private String lot;
}
