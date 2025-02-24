package com.swyp.mema.database.station.model;

import com.swyp.mema.domain.station.model.Station;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class _TransferStation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curStationId")
    private _Station curStation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transferStationId")
    private _Station transferStation;

    @Setter
    private Integer transferTime;

    public void printAll(){
        String curCode = curStation.getLineName() + "_:" + curStation.getStationName();
        String nextCode = transferStation.getLineName() + "_:" + transferStation.getStationName();
        System.out.println("Transfer station: " + curCode + "->" + nextCode + ", time: " + transferTime);
    }
}
