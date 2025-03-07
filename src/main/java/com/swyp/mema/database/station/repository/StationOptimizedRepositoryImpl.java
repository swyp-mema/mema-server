package com.swyp.mema.database.station.repository;

import com.swyp.mema.database.station.model._NextStation;
import com.swyp.mema.database.station.model._Station;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class StationOptimizedRepositoryImpl implements StationOptimizedRepository{

    @PersistenceContext
    private EntityManager em;

    /**
     * _Station 엔티티 + 엔티티와 연관관계가 있는 모든 엔티티들을 한번에 로딩
     * @return
     */
    @Override
    public List<_Station> findAllForRuntimeAlgorithm(){

        //station 선택 & nextStations 매핑
        List<_Station> stations = em.createQuery(
                "SELECT s FROM _Station s " +
                        "LEFT JOIN FETCH s.nextStations ns ",
                _Station.class
        ).getResultList();

        //station의 routes 매핑
        em.createQuery(
                "SELECT DISTINCT s FROM _Station s " +
                        "LEFT JOIN FETCH s.routes sr " +
                        "WHERE s IN :stations",
                _Station.class
        ).setParameter("stations", stations)
        .getResultList();

        //_NextStation의 연관관계 매핑
        em.createQuery(
                "SELECT DISTINCT ns FROM _NextStation  ns " +
                        "LEFT JOIN FETCH ns.routes nsr " +
                        "LEFT JOIN FETCH ns.nextStation nsn " +
                        "LEFT JOIN FETCH ns.curStation nsc ",
                _NextStation.class
        ).getResultList();



        //station의 transferStations 매핑
        em.createQuery(
                        "SELECT DISTINCT s " +
                                "FROM _Station  s " +
                                "LEFT JOIN FETCH s.transferStations ts " +
                                    "LEFT JOIN FETCH ts.curStation tsc " +
                                    "LEFT JOIN FETCH ts.transferStation tst " +
                                "WHERE s IN :stations",
                        _Station.class
                ).setParameter("stations", stations)
                .getResultList();

        return stations;
    }



}
