package com.swyp.mema.database.station.logic;

import com.swyp.mema.database.station.logic.subbuilder.NextStationBuilder;
import com.swyp.mema.database.station.logic.subbuilder.StationDataBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TotalDataBuilder {

    private final StationDataBuilder stationDataBuilder;
    private final NextStationBuilder nextStationBuilder;

    public void buildDB(){

        stationDataBuilder.createStationData();
        nextStationBuilder.buildStationRelation();
    }
}
