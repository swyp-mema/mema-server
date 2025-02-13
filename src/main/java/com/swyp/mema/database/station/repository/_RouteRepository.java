package com.swyp.mema.database.station.repository;

import com.swyp.mema.database.station.model._Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface _RouteRepository extends JpaRepository<_Route, String> {

    boolean existsByRoute(String route);
    _Route findByRoute(String route);

    List<_Route> findByLine(String line);
}
