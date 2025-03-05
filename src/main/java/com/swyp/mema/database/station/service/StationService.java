package com.swyp.mema.database.station.service;

import com.swyp.mema.database.station.dto.response.StationRes;
import com.swyp.mema.database.station.dto.response.TotalStationRes;
import com.swyp.mema.database.station.repository.StationCustomRepository;
import com.swyp.mema.database.station.util.StationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationCustomRepository stationRepository;
    private final StationConverter converter;

    /**
     * _Station 테이블의 값을 가져와서 모든 지하철 조회
     * _Station 테이블 저장
     */
    @Transactional(readOnly = true)
    public TotalStationRes getStationInfo() {

        List<StationRes> stationRes = stationRepository.findStationsWithLocation();
        return new TotalStationRes(stationRes.size(), stationRes);
    }

}
