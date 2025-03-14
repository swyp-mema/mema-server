package com.swyp.mema.database.station.model;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name="transfer_station_current_station_idx", columnList = "curStationId")
})
public class _TransferStation {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curStationId")
    private _Station curStation;

    @ManyToOne(fetch = FetchType.LAZY)
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
