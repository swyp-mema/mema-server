package com.swyp.mema.database.station.logic.subbuilder;

import com.swyp.mema.database.openapi.location.logic.StationMasterService;
import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.database.station.repository.StationRepository;
import com.swyp.mema.database.station.util.AddrToCodeConverter;
import com.swyp.mema.database.station.util.ExcelReader;
import com.swyp.mema.database.station.util.StationConverter;
import com.swyp.mema.database.station.util.StringCleaner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StationDataBuilderService {

    private final StationRepository stationRepository;
    private final ExcelReader excelData;
    private final AddrToCodeConverter addrToCodeConverter;
    private final StringCleaner stringCleaner;
    private final StationConverter stationConverter;
    private final ExcelReader excelReader;
    private final StationMasterService stationMasterService;


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

    @Transactional
    public List<_Station> addLocationData() {

        List<_Station> stationList = stationRepository.findAll();
        HashMap<String, _Station> apiResponse = stationConverter.listToHashmap(
                stationConverter.apiResponseToStationList(
                        stationMasterService.getSubwayMasterByAPI()));
        HashMap<String, _Station> excelData = stationConverter.listToHashmap(
                excelReader.readLocationData());

        for(_Station station : stationList){

            // api 응답에서 살펴보기
            if (apiResponse.containsKey(stringCleaner.createCode(station))) {

                //api 응답에 좌표 존재
                _Station res = apiResponse.get(stringCleaner.createCode(station));
                station.setLoc(res.getLat(), res.getLot());
                continue;
            }

            if(excelData.containsKey(stringCleaner.createCode(station))){
                //excel 데이터에 해당 역 존재
                _Station res = excelData.get(stringCleaner.createCode(station));

                if (!res.getLat().isEmpty()) {
                    // excel 데이터에 위경도 좌표가 존재
                    station.setLoc(res.getLat(), res.getLot());
                    continue;
                }
                Pair<String, String> loc = addrToCodeConverter.getCoord(res.getAddress());
                station.setLoc(loc.getFirst(), loc.getSecond());
                continue;
            }

            // 데이터가 없다면 같은 이름의 다른 호선(환승역)의 데이터라도 가져오도록 시도
            station.getTransferStations().stream().filter(ts -> ts.getTransferStation().getLat() != null).findFirst().ifPresent(
                    ts ->
                        station.setLoc(ts.getTransferStation().getLat(), ts.getTransferStation().getLot()));

        }
        return stationList;
    }
}
