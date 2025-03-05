package com.swyp.mema.domain.station.dto.response.subwayInfo;

import java.util.List;


import lombok.Builder;
import lombok.Getter;

@Getter
public class TotalStationRes {

	int pageNo;
	int numOfRows;
	int totalCount;
	List<SingleStationRes> stationList;

	@Builder
	public TotalStationRes(int pageNo, int numOfRows, int totalCount, List<SingleStationRes> stationList) {
		this.pageNo = pageNo;
		this.numOfRows = numOfRows;
		this.totalCount = totalCount;
		this.stationList = stationList;
	}
}
