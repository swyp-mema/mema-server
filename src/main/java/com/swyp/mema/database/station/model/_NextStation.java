package com.swyp.mema.database.station.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class _NextStation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curStationId")
    private _Station curStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nextStationId")
    private _Station nextStation;

    @Setter
    private Integer moveTime; //이동시간

    @ManyToMany
    @JoinTable(name="NEXT_STATION_ROUTE",
            joinColumns = @JoinColumn(name = "NEXT_STATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROUTE"))
    private Set<_Route> routes;

    public void addRoute(_Route route) {

        if (routes == null) {
            routes = new HashSet<>();
        }
        routes.add(route);
    }

    public void printData(){
        String line, curName, nextName;
        line = curStation.getLineName();
        curName = curStation.getStationName();
        nextName = nextStation.getStationName();
        System.out.println("line: " + line + ",  " + curName + " -> " + nextName + ", move time: " + moveTime);
    }
}
