package com.swyp.mema.database.station.model;

import com.swyp.mema.domain.midloc.service.structures.NextStation;
import com.swyp.mema.domain.midloc.service.structures.TransferStation;
import com.swyp.mema.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class _Station extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Setter
    private String scheduleId;

    @Setter
    private String realtimeId;

    @Column(nullable = false)
    private String stationName;	// 역이름

    @Column(nullable = false)
    private String lineName;	// 호선 정보

    private String lat;	// 위도

    private String lot;	// 경도

    private String address;

    @OneToMany(mappedBy = "curStation", cascade = CascadeType.ALL)
    private List<_NextStation> nextStations;

    @OneToMany(mappedBy = "curStation", cascade = CascadeType.ALL)
    private List<_TransferStation> transferStations;

    @ManyToMany
    @JoinTable(name="STATION_ROUTE",
            joinColumns = @JoinColumn(name = "STATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROUTE"))
    private Set<_Route> routes;

    /* Add Info*/
    public void addRoute(_Route route) {

        if (routes == null) {
            routes = new HashSet<>();
        }
        routes.add(route);
    }
    public void addTransferStation(_TransferStation transferStation) {
        transferStations.add(transferStation);
    }

    public void addNextStation(_NextStation nextStation) {
        nextStations.add(nextStation);
    }

    public void setLoc(String lat, String lot) {
        this.lat = lat;
        this.lot = lot;
    }

    public void setInfo(String stationName, String lineName, String address) {

        this.stationName = stationName;
        this.lineName = lineName;
        this.address = address;
    }

    public void addRoutes(List<_Route> routes) {
        if (this.routes == null) {
            this.routes = new HashSet<>();
        }
        this.routes.addAll(routes);
    }

    public void printAll(){

        System.out.println("name: " + stationName + ", line: " + lineName + ", lat: " + lat + ", lot: " + lot + ", ID: " + scheduleId);
    }
}
