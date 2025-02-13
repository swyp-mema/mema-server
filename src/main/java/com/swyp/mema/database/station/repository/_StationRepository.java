package com.swyp.mema.database.station.repository;

import com.swyp.mema.database.station.model._Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface _StationRepository extends JpaRepository<_Station, String> {

    public List<_Station> findByStationName(String stationName);
    public _Station findByLineNameAndStationName(String lineName, String stationName);
}
