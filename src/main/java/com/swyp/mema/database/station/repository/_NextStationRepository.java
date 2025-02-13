package com.swyp.mema.database.station.repository;

import com.swyp.mema.database.station.model._NextStation;
import com.swyp.mema.database.station.model._Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface _NextStationRepository extends JpaRepository<_NextStation, String> {

    public _NextStation findById(long id);
    public _NextStation findByCurStationAndNextStation(_Station startStation, _Station endStation);
    public Boolean existsByCurStationAndNextStation(_Station startStation, _Station endStation);
}
