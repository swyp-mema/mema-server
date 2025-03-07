package com.swyp.mema.database.station.repository;

import com.swyp.mema.database.station.model._Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StationRepository extends JpaRepository<_Station, String>, StationOptimizedRepository{

    List<_Station> findByStationName(String stationName);
    List<_Station> findByLineName(String lineName);
    _Station findByLineNameAndStationName(String lineName, String stationName);
    _Station findByScheduleId(String scheduleId);


}
