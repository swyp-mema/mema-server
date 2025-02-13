package com.swyp.mema.database.station.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class _Route implements Comparable<_Route> {

    @Id
    private String route;

    private String line;

    //  평일의 차 숫자
    private Integer num1;
    //  평일의 카운트 시간 (단위 : 분)
    private Integer time1;

    //  토요일의 차 숫자
    private Integer num2;
    //  토요일의 카운트 시간 (단위 : 분)
    private Integer time2;

    //  일요일, 공휴일의 차 숫자
    private Integer num3;
    //  일요일, 공휴일의 카운트 시간(단위 : 분)
    private Integer time3;

    @Override
    public int compareTo(_Route o) {
        return route.compareTo(o.route);
    }

    public _Route initNums(){
        num1 = 0;
        num2 = 0;
        num3 = 0;
        time1 = 0;
        time2 = 0;
        time3 = 0;
        return this;
    }

    public void addNum(int day, int num, int time) {

        switch (day){
            case 1:
                num1 = num;
                time1 = time;
                break;
            case 2:
                num2 = num;
                time2 = time;
                break;
            case 3:
                num3 = num;
                time3 = time;
                break;
        }
    }

    public int getNum(int day){
        return switch (day) {
            case 1 -> num1;
            case 2 -> num2;
            case 3 -> num3;
            default -> 0;
        };
    }
}
