package com.swyp.mema.database.station.repository;

import com.swyp.mema.database.station.model._Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationOptimizedRepository {


    List<_Station> findAllForRuntimeAlgorithm();
}
