package com.swyp.mema.domain.charge.repository;

import com.swyp.mema.domain.charge.model.ChargeMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeMemberRepository extends JpaRepository<ChargeMember, Long> {

}
