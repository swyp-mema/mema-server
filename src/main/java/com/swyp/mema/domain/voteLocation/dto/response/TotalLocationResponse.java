package com.swyp.mema.domain.voteLocation.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "유저 프로필 응답")
public class TotalLocationResponse {

	@Schema(description = "유저들이 선택한 역", example = "[왕십리, 평촌, 강남]  **리스트")
	private List<String> startStationList;

	@Schema(description = "중간역", example = "부평")
	private String arrivalStation;

	private String arrivalLine;

}

