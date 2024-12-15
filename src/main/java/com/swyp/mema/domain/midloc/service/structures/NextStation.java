package com.swyp.mema.domain.midloc.service.structures;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class NextStation {

    String stationCode;
    @Setter
    int waitTime; //배차간격

    public void printData(){
        System.out.println(stationCode + ", waitTime: " + waitTime);
    }
}
