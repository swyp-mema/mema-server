package com.swyp.mema.database.station.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TotalStationRes {

    int totalCount;
    List<StationRes> stationList;

    @Builder
    public TotalStationRes(int totalCount, List<StationRes> stationList) {
        this.totalCount = totalCount;
        this.stationList = stationList;
    }
}
