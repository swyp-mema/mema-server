package com.swyp.mema.domain.voteLocation.dto.response;

import com.swyp.mema.database.station.dto.response.subwayInfo.SingleStationRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "유저 프로필 응답")
public class MidLocationTotalRes {

    @Schema(description = "유저들이 선택한 역", example = "[왕십리, 평촌, 강남]  **리스트")
    private List<MidLocationRes> users;

    @Schema(description = "중간역", example = "부평")
    private SingleStationRes midStation;

    public void printAll(){
        System.out.println("중간역: ");
        midStation.printAll();

        for(MidLocationRes startStation : users){
            startStation.printAll();
        }
    }

}
