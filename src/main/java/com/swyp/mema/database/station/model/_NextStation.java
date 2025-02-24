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

    // current station -> next station 으로 가는 요일별 배차 수
    private Integer num1;
    private Integer num2;
    private Integer num3;



    public void printData(){
        String line, curName, nextName;
        line = curStation.getLineName();
        curName = curStation.getStationName();
        nextName = nextStation.getStationName();
        System.out.println('\n'+line + ", " + curName + " -> " +line + ", " + nextName + ", move time: " + moveTime);
    }

    public void addNum(int day, int num) {

        switch (day){
            case 1:
                if(num1 == null) num1 = 0;
                num1 += num;
                break;
            case 2:
                if(num2 == null) num2 = 0;
                num2 += num;
                break;
            case 3:
                if(num3 == null) num3 = 0;
                num3 += num;
                break;
        }
    }
}
