package com.swyp.mema.domain.midloc.service.structures;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransferStation {

    String stationCode;
    int transferTime;   //환승시간
}
