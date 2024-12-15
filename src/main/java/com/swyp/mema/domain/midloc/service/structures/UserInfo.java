package com.swyp.mema.domain.midloc.service.structures;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class UserInfo {

    public UserInfo(String userName, String startStation, String startLine) {
        arrivalTimes = new HashMap<String, Integer>();
        this.userName = userName;
        this.startStation = startStation;
        this.startLine = startLine;
    }

    HashMap<String, Integer> arrivalTimes;
    String userName;
    String startStation;
    String startLine;

}
