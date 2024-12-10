package com.swyp.mema.domain.locaction.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TotalStationResponse {

	int pageNo;
	int numOfRows;
	int totalCount;
	List<StationResponse> stationList;

	@Builder
	public TotalStationResponse(int pageNo, int numOfRows, int totalCount, List<StationResponse> stationList) {
		this.pageNo = pageNo;
		this.numOfRows = numOfRows;
		this.totalCount = totalCount;
		this.stationList = stationList;
	}
}
