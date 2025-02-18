package com.swyp.mema.database.station.logic.subbuilder;

import com.swyp.mema.database.openapi.location.logic.StationMasterService;
import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.database.station.repository._StationRepository;
import com.swyp.mema.database.station.util.AddrToCodeConverter;
import com.swyp.mema.database.station.util.ExcelReader;
import com.swyp.mema.database.station.util.StationConverter;
import com.swyp.mema.database.station.util.StringCleaner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StationDataBuilderService {

    private final _StationRepository stationRepository;
    private final StationMasterService subwayLocationService;
    private final ExcelReader excelData;
    private final AddrToCodeConverter addrToCodeConverter;
    private final StringCleaner stringCleaner;
    private final StationConverter stationConverter;


    /**
     *      Public Method
     *      Station 데이터를 생성합니다.
     */
    @Transactional
    public void createStationData(){

        List<_Station> stations = createTable();
        stationRepository.saveAll(stations);
    }

    /**
     * 엑셀 파일의 역 id 테이블을 읽어 역명, 호선, id값 정보를 가진 station list로 만들어 반환합니다.
     */
    public List<_Station> createTable(){

        List<_Station> stations = new ArrayList<>();

        ArrayList<ArrayList<String>> datas = excelData.readFile("/scheduleIds.xlsx");
        for(ArrayList<String> data : datas){

            String lineName, stationName, scheduleId;
            lineName = data.get(0);
            stationName = data.get(1);
            scheduleId = data.get(2);
            if (scheduleId.contains(".")) {

                scheduleId = String.valueOf((int) Double.parseDouble(scheduleId));
            }

            _Station station = _Station.builder()
                    .stationName(stationName)
                    .lineName(lineName)
                    .scheduleId(scheduleId)
                    .build();

            stations.add(station);
        }

        return stations;
    }

}
