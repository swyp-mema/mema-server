package com.swyp.mema.database.station.logic;

import com.swyp.mema.database.station.model._NextStation;
import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.database.station.repository._NextStationRepository;
import com.swyp.mema.database.station.repository._StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ControllStationData {

    private final _StationRepository stationRepository;
    private final _NextStationRepository nextStationRepository;

    public void addNextStation(String line, String curStationName, String nextStationName) {

        _Station curStation = stationRepository.findByLineNameAndStationName(line, curStationName);
        _Station nextStation = stationRepository.findByLineNameAndStationName(line, nextStationName);
        if(nextStationRepository.existsByCurStationAndNextStation(curStation,nextStation)) {
            return;
        }

        _NextStation newNext = _NextStation.builder()
                .curStation(curStation)
                .nextStation(nextStation)
                .build();
        nextStationRepository.save(newNext);
        newNext.printData();
    }

}
