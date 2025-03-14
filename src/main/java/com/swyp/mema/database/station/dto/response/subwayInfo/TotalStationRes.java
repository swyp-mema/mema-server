package com.swyp.mema.database.station.dto.response.subwayInfo;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
