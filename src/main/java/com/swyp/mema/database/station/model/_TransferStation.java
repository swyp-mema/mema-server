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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curStationId")
    private _Station curStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transferStationId")
    private _Station transferStation;

    @Setter
    private Integer transferTime;
}
