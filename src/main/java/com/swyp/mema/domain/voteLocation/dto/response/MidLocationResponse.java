package com.swyp.mema.domain.voteLocation.dto.response;

import com.swyp.mema.domain.station.dto.response.subwayInfo.SingleStationResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "유저 프로필 응답")
public class MidLocationResponse {

    @Schema(description = "유저들이 선택한 역", example = "[왕십리, 평촌, 강남]  **리스트")
    private List<SingleStationResponse> startStationList;

    @Schema(description = "중간역", example = "부평")
    private SingleStationResponse midStation;

}
