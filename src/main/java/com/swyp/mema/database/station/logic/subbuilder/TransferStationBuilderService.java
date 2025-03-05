package com.swyp.mema.database.station.logic.subbuilder;

import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.database.station.model._TransferStation;
import com.swyp.mema.database.station.repository.StationRepository;
import com.swyp.mema.database.station.repository._TransferStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferStationBuilderService {

    private final StationRepository stationRepository;
    private final _TransferStationRepository transferStationRepository;

    /**
     *      Public Method
     *      Transfer Station 데이터를 구축합니다.
     *
     *  이름이 같은 station끼리 묶어 transfer station 생성
     */
    public void buildTransferStation() {

        List<_Station> stationList = stationRepository.findAll();
        HashMap<String, ArrayList<String>> stationNameMap = new HashMap<>();
        for (_Station station : stationList) {

            String stationName = station.getStationName();
            String line = station.getLineName();
            if(!stationNameMap.containsKey(stationName)) {
                stationNameMap.put(stationName, new ArrayList<String>());
            }
            stationNameMap.get(stationName).add(line);
        }

        for (Map.Entry<String, ArrayList<String>> entry : stationNameMap.entrySet()) {

            String stationName = entry.getKey();
            ArrayList<String> lines = entry.getValue();
            int size = lines.size();
            for (String curLine : lines) {
                for(String transferLine : lines){
                    if(curLine.equals(transferLine)) continue;
                    int transferTime = getTransferTime(stationName, curLine, transferLine, size);
                    _Station curStation = stationRepository.findByLineNameAndStationName(curLine, stationName);
                    _Station transferStation = stationRepository.findByLineNameAndStationName(transferLine, stationName);
                    _TransferStation newTransferStation = _TransferStation.builder()
                            .curStation(curStation)
                            .transferStation(transferStation)
                            .transferTime(transferTime).build();

                    curStation.addTransferStation(newTransferStation);
                    transferStationRepository.save(newTransferStation);
                }
            }
        }
    }

    /**
     * 환승시간 계산 로직
     *
     *  ###     현재는 임시로직      ###
     *
     * @return  환승시간
     */
    private int getTransferTime(String stationName, String curLine, String transferLine, int size) {

        return 2 * size - 1;
    }
}
