package com.swyp.mema.database.station.util;

import com.swyp.mema.database.station.model._Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StationConverter {

    private final StringCleaner stringCleaner;

    public HashMap<String, _Station> list2Hashmap(List<_Station> stations) {

        HashMap<String, _Station> result = new HashMap<>();
        for(_Station station : stations) {

            String code = stringCleaner.createCode(station.getLineName(), station.getStationName());
            result.put(code, station);
        }

        return result;
    }
}