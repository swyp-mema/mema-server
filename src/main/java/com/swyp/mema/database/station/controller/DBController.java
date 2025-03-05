package com.swyp.mema.database.station.controller;

import com.swyp.mema.database.station.logic.ControllStationDataService;
import com.swyp.mema.database.station.logic.TotalDataBuilderService;
import com.swyp.mema.database.station.logic.subbuilder.NextStationBuilderService;
import com.swyp.mema.database.station.logic.subbuilder.RouteBuilderService;
import com.swyp.mema.database.station.logic.subbuilder.StationDataBuilderService;
import com.swyp.mema.database.station.logic.subbuilder.TransferStationBuilderService;
import com.swyp.mema.database.station.model._NextStation;
import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.database.station.repository._NextStationRepository;
import com.swyp.mema.database.station.repository._StationRepository;
import com.swyp.mema.database.station.util.ExcelReader;
import com.swyp.mema.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
public class DBController {

    private final ExcelReader excelReader;
    private final _NextStationRepository nextStationRepository;
    private final ControllStationDataService controllStationDataService;
    private final NextStationBuilderService nextStationBuilderService;
    private final RouteBuilderService routeBuilderService;
    private final StationDataBuilderService stationDataBuilderService;
    private final TransferStationBuilderService transferStationBuilderService;
    private final TotalDataBuilderService totalDataBuilderService;
    private final _StationRepository stationRepository;

    @GetMapping("/DB/test1")
    public ResponseEntity<String> DBTest1() {

        totalDataBuilderService.buildDB();
        return ResponseEntity.ok("");
    }

    @GetMapping("/DB/test2")
    public ResponseEntity<String> DBtest2() {

        nextStationBuilderService.buildExcludeLineNextStation();
        return ResponseEntity.ok("");
    }

    @GetMapping("/DB/test3")
    public ResponseEntity<String> DBtest3() {

        transferStationBuilderService.buildTransferStation();
//        stationDataBuilderService.addLocationData();
        return ResponseEntity.ok("");
    }
    @GetMapping("/DB/addNext/{line}/{curStationName}/{nextStationName}")
    public ResponseEntity<String> addNext(@PathVariable String line,
                                          @PathVariable String curStationName,
                                          @PathVariable String nextStationName) {

        controllStationDataService.addNextStation(line, curStationName, nextStationName);
        return ResponseEntity.ok("");
    }

    @GetMapping("/DB/nextStation/{nextStationId}")
    public ResponseEntity<String> getNextStation(@PathVariable String nextStationId) {

        _NextStation nextStationEntity = nextStationRepository.findById(Long.parseLong(nextStationId));
        nextStationEntity.printData();
        return ResponseEntity.ok("");
    }

    @GetMapping("/DB/stationInfo/{line}/{stationName}")
    public ResponseEntity<String> getStationInfo(@PathVariable String line,
                                                 @PathVariable String stationName) {

        _Station station = stationRepository.findByLineNameAndStationName(line, stationName);
        station.printAll();
        return ResponseEntity.ok("");
    }
}
