package com.swyp.mema.database.station.repository;

import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.database.station.model._TransferStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface _TransferStationRepository extends JpaRepository<_TransferStation, String> {

    public _TransferStation findByCurStationAndTransferStation(_Station startStation, _Station endStation);
    public Boolean existsByCurStationAndTransferStation(_Station startStation, _Station endStation);
}
