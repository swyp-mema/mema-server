package com.swyp.mema.database.station.util;

import com.swyp.mema.database.openapi.location.response.TotalSubwayMasterResponse;
import com.swyp.mema.database.station.model._Station;
import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StationConverter {

    private final StringCleaner stringCleaner;

    public HashMap<String, _Station> listToHashmap(List<_Station> stations) {

        HashMap<String, _Station> result = new HashMap<>();
        for(_Station station : stations) {

            String code = stringCleaner.createCode(station);
            result.put(code, station);
        }

        return result;
    }

    public List<_Station> apiResponseToStationList(TotalSubwayMasterResponse apiResponse){

        List<_Station> result = new ArrayList<>();

        return apiResponse.getMasterList().stream().map(res -> _Station.builder()
                        .stationName(res.getStationName())
                        .lineName((res.getLine()))
                        .lat(res.getLat())
                        .lot(res.getLot()).build())
                .toList();
    }
}
