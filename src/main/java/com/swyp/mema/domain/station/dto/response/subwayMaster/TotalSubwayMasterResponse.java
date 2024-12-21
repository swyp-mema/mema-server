package com.swyp.mema.domain.station.dto.response.subwayMaster;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TotalSubwayMasterResponse {

	private List<SubwayMasterResponse> masterList;

}
