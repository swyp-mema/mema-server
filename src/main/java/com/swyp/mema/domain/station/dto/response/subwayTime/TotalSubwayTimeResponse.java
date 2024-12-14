package com.swyp.mema.domain.station.dto.response.subwayTime;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TotalSubwayTimeResponse {
	List<SubwayTimeResponse> subwayTimeList;
}
