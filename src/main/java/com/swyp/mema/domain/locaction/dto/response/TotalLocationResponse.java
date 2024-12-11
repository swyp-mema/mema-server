package com.swyp.mema.domain.locaction.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TotalLocationResponse {

	private List<String> startStationList;
	private String arrivalStation;
}
