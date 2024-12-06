package com.swyp.mema.domain.charge.repository;

import com.swyp.mema.domain.charge.model.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Long> {

    List<Charge> findByMeetId(Long meetId);
}
