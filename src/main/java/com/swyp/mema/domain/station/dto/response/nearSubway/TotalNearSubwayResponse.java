package com.swyp.mema.domain.station.dto.response.nearSubway;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TotalNearSubwayResponse {
	List<NearSubwayResponse> subwayTimeList;
}
