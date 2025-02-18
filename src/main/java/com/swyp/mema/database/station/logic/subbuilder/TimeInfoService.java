package com.swyp.mema.database.station.logic.subbuilder;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TimeInfoService {

    /**
     *
     * 배차 측정 시간 설정 클래스
     *
     */

    //평일
    int startTime0 = 16;
    int startMinute0 = 0;
    int endTime0 = 22;
    int endMinute0 = 0;

    //토요일
    int startTime1 = 11;
    int startMinute1 = 0;
    int endTime1 = 22;
    int endMinute1 = 0;

    //주말, 공휴일
    int startTime2 = 11;
    int startMinute2 = 0;
    int endTime2 = 22;
    int endMinute2 = 0;

    public List<List<Integer>> getCountTime(){

        return Arrays.asList(
                Arrays.asList(startTime0 * 60 + startMinute0, endTime0 * 60 + endMinute0),
                Arrays.asList(startTime1 * 60 + startMinute1, endTime1 * 60 + endMinute1),
                Arrays.asList(startTime2 * 60 + startMinute2, endTime2 * 60 + endMinute2)
        );
    }
}
