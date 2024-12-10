package com.swyp.mema.domain.locaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.mema.domain.locaction.model.Station;

public interface StationRepository extends JpaRepository<Station, String> {
}
