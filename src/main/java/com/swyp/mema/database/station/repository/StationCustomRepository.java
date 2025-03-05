package com.swyp.mema.database.station.repository;

import com.swyp.mema.database.station.dto.response.StationRes;

import java.util.List;

public interface StationCustomRepository {

    List<StationRes> findStationsWithLocation();
}
