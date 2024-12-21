package com.swyp.mema.domain.station.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.mema.domain.station.model.Station;

public interface StationRepository extends JpaRepository<Station, String> {
}
