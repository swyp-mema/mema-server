package com.swyp.mema.domain.locaction.dto.response;

import java.util.List;

import com.swyp.mema.domain.locaction.dto.response.OpenApiBasicResponse.Item;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LocationResponse {

	int pageNo;
	int numOfRows;
	int totalCount;
	List<Item> subwayList;

	@Builder
	public LocationResponse(int pageNo, int numOfRows, int totalCount, List<Item> subwayList) {
		this.pageNo = pageNo;
		this.numOfRows = numOfRows;
		this.totalCount = totalCount;
		this.subwayList = subwayList;
	}
}
