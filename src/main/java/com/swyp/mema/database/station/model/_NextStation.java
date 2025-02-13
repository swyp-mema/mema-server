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



    public void printData(){
        String line, curName, nextName;
        line = curStation.getLineName();
        curName = curStation.getStationName();
        nextName = nextStation.getStationName();
        System.out.println('\n'+line + ", " + curName + " -> " +line + ", " + nextName + ", move time: " + moveTime);
    }
}
