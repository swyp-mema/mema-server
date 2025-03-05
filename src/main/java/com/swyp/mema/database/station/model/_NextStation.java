package com.swyp.mema.database.station.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.RouteMatcher;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class _NextStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curStationId")
    private _Station curStation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nextStationId")
    private _Station nextStation;

    @Setter
    private Integer moveTime; //이동시간

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="NEXTSTATION_ROUTE",
            joinColumns = @JoinColumn(name = "NEXTSTATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROUTE"))
    private Set<_Route> routes;


    public void printData(){
        String line, curName, nextName;
        line = curStation.getLineName();
        curName = curStation.getStationName();
        nextName = nextStation.getStationName();
        System.out.println('\n'+ "next station: " + line + ", " + curName + " -> " +line + ", " + nextName + ", move time: " + moveTime);
    }

    public void addRoute(_Route route) {
        if(routes == null) routes = new HashSet<>();
        this.routes.add(route);
    }
}
