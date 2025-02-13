package com.swyp.mema.database.station.controller;

import com.swyp.mema.database.station.logic.ControllStationData;
import com.swyp.mema.database.station.logic.subbuilder.NextStationBuilder;
import com.swyp.mema.database.station.logic.subbuilder.RouteBuilder;
import com.swyp.mema.database.station.logic.subbuilder.StationDataBuilder;
import com.swyp.mema.database.station.model._NextStation;
import com.swyp.mema.database.station.repository._NextStationRepository;
import com.swyp.mema.database.station.util.ExcelReader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class DBController {

    private final ExcelReader excelReader;
    private final _NextStationRepository nextStationRepository;
    private final ControllStationData controllStationData;
    private final NextStationBuilder nextStationBuilder;
    private final RouteBuilder routeBuilder;
    private final StationDataBuilder stationDataBuilder;

    @GetMapping("/DB/test1")
    public ResponseEntity<String> DBTest1() {

        stationDataBuilder.createStationData();
        return ResponseEntity.ok("");
    }

    @GetMapping("/DB/test2")
    public ResponseEntity<String> DBtest2() {

        routeBuilder.buildIncludeRoute();
//        routeBuilder.buildExcludeRoute();
//        nextStationBuilder.buildStationRelation();
        return ResponseEntity.ok("");
    }

    @GetMapping("/DB/test3")
    public ResponseEntity<String> DBtest3() {

        nextStationBuilder.buildIncludeLineNextStation();
        return ResponseEntity.ok("");
    }
    @GetMapping("/DB/addNext/{line}/{curStationName}/{nextStationName}")
    public ResponseEntity<String> addNext(@PathVariable String line,
                                          @PathVariable String curStationName,
                                          @PathVariable String nextStationName) {

        controllStationData.addNextStation(line, curStationName, nextStationName);
        return ResponseEntity.ok("");
    }

    @GetMapping("/DB/nextStation/{nextStationId}")
    public ResponseEntity<String> getNextStation(@PathVariable String nextStationId) {

        _NextStation nextStationEntity = nextStationRepository.findById(Long.parseLong(nextStationId));
        nextStationEntity.printData();
        return ResponseEntity.ok("");
    }
}
