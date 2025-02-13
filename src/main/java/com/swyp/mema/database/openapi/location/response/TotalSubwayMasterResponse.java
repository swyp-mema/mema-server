package com.swyp.mema.database.openapi.location.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TotalSubwayMasterResponse {

	private List<SubwayMasterResponse> masterList;

}
