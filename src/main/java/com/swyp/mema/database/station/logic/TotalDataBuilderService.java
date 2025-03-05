package com.swyp.mema.database.station.logic;

import com.swyp.mema.database.station.logic.subbuilder.NextStationBuilderService;
import com.swyp.mema.database.station.logic.subbuilder.RouteBuilderService;
import com.swyp.mema.database.station.logic.subbuilder.StationDataBuilderService;
import com.swyp.mema.database.station.logic.subbuilder.TransferStationBuilderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TotalDataBuilderService {

    private final StationDataBuilderService stationDataBuilderService;
    private final NextStationBuilderService nextStationBuilderService;
    private final RouteBuilderService routeBuilderService;
    private final TransferStationBuilderService transferStationBuilderService;

    public void buildDB(){

        stationDataBuilderService.createStationData();
        routeBuilderService.buildExcludeRoute();
        nextStationBuilderService.buildExcludeLineNextStation();
        transferStationBuilderService.buildTransferStation();
        stationDataBuilderService.addLocationData();
    }
}
