package com.swyp.mema.database.station.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StationRes {

    private String stationName;
    private String lineName;
    private String lat;	// 위도
    private String lot; // 경도

}
