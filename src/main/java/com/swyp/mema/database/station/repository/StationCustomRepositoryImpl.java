package com.swyp.mema.database.station.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyp.mema.database.station.dto.response.StationRes;
import com.swyp.mema.database.station.model.Q_Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StationCustomRepositoryImpl implements StationCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StationRes> findStationsWithLocation() {

        Q_Station station = Q_Station._Station;

        return queryFactory
                .select(Projections.constructor(StationRes.class,
                        station.stationName,
                        station.lineName,
                        station.lat,
                        station.lot))
                .from(station)
                .fetch();
    }
}
