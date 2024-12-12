package com.swyp.mema.domain.station.dto.response;

import java.util.List;


import lombok.Builder;
import lombok.Getter;

@Getter
public class TotalStationResponse {

	int pageNo;
	int numOfRows;
	int totalCount;
	List<SingleStationResponse> stationList;

	@Builder
	public TotalStationResponse(int pageNo, int numOfRows, int totalCount, List<SingleStationResponse> stationList) {
		this.pageNo = pageNo;
		this.numOfRows = numOfRows;
		this.totalCount = totalCount;
		this.stationList = stationList;
	}
}
