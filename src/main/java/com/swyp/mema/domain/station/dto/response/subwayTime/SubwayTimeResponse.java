package com.swyp.mema.domain.station.dto.response.subwayTime;

import lombok.Builder;
import lombok.Getter;

/*
	시간표
 */
@Getter
@Builder
public class SubwayTimeResponse {

	// 지하철역 ID
	private String stationId;

	// 지하철역명
	private String stationName;

	// 지하철 노선 ID
	private String routeId;

	// 요일 구분 코드 (01: 평일, 02: 토요일, 03: 일요일)
	private String dailyTypeCode;

	// 출발 시간
	private String departureTime;

	// 도착 시간
	private String arrivalTime;

	// 종점 지하철역 ID
	private String endStationId;

	// 종점 지하철역명
	private String endStationName;

	// 상하행 구분 코드 (U: 상행, D: 하행)
	private String upDownTypeCode;

}
