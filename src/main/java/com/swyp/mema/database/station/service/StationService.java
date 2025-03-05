package com.swyp.mema.database.station.service;

import com.swyp.mema.database.station.dto.response.StationRes;
import com.swyp.mema.database.station.dto.response.TotalStationRes;
import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.database.station.repository.StationRepository;
import com.swyp.mema.database.station.util.StationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;
    private final StationConverter converter;

    /**
     * _Station 테이블의 값을 가져와서 모든 지하철 조회
     * _Station 테이블 저장
     */
    @Transactional(readOnly = true)
    public TotalStationRes getStationInfo() {

        List<_Station> all = stationRepository.findAll();

        List<StationRes> stationRes = converter.toStationRes(all);
        return new TotalStationRes(stationRes.size(), stationRes);
    }

}
