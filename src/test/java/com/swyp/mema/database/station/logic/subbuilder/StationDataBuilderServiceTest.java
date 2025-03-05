package com.swyp.mema.database.station.logic.subbuilder;

import com.swyp.mema.database.openapi.location.logic.StationMasterService;
import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.database.station.repository.StationRepository;
import com.swyp.mema.database.station.util.ExcelReader;
import com.swyp.mema.database.station.util.StationConverter;
import com.swyp.mema.database.station.util.StringCleaner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class StationDataBuilderServiceTest {

    @Autowired StationDataBuilderService builder;
    @Autowired
    StationRepository stationRepository;
    @Autowired
    private StationConverter stationConverter;
    @Autowired
    private StationMasterService stationMasterService;
    @Autowired
    private StringCleaner stringCleaner;
    @Autowired
    private ExcelReader excelReader;

    @Ignore("위치 데이터 빌드 할 때만 테스트 하세요.")
    @Test
    public void locationBuild_Success_Test() {

        List<_Station> stationList = builder.addLocationData();
        List<_Station> list = stationList.stream().filter(s -> s.getLat() == null).toList();
        System.out.println(list.size());
        list.forEach(s -> System.out.println(s.getLineName() + "-" + s.getStationName()));
        assert list.isEmpty();
    }

    @Ignore("위치 데이터 빌드 할 때만 테스트 하세요.")
    @Test
    public void dataMatch_Api_Test(){

        HashMap<String, _Station> stationMap = stationConverter.listToHashmap(stationRepository.findAll());
        List<_Station> apiRes = stationConverter.apiResponseToStationList(
                stationMasterService.getSubwayMasterByAPI());

        List<_Station> list = apiRes.stream().filter(s -> !stationMap.containsKey(stringCleaner.createCode(s))).toList();
        System.out.println(list.size());
        list.forEach(s -> System.out.println(stringCleaner.createCode(s)));
    }

    @Ignore("위치 데이터 빌드 할 때만 테스트 하세요.")
    @Test
    public void dataMatch_excel_Test(){

        HashMap<String, _Station> stationMap = stationConverter.listToHashmap(stationRepository.findAll());
        List<_Station> excelData = excelReader.readLocationData();

        List<_Station> list = excelData.stream().filter(s -> !stationMap.containsKey(stringCleaner.createCode(s))).toList();
        System.out.println(list.size());
        list.forEach(s -> System.out.println(stringCleaner.createCode(s)));
    }
}