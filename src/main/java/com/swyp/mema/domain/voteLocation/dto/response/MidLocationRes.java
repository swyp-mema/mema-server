package com.swyp.mema.domain.voteLocation.dto.response;

import com.swyp.mema.domain.station.dto.response.subwayInfo.SingleStationRes;
import lombok.Builder;

import java.util.List;

@Builder
public class MidLocationRes {

    Long userId;
    String nickname;
    Long puzId;
    String puzColor;
    String role;
    Integer time;
    String stationName;		// 출발 위치 역이름
    String stationRoute;	// 출발 위치 호선 정보

    List<SingleStationRes> stationPath;

    public void printAll(){
        System.out.println("\n" + userId + " - " + nickname + " - " + puzId + " - " + puzColor + "\n출발역:" + stationRoute + "-" +stationName + ", time: " + time);
        for(SingleStationRes singleStationRes : stationPath){
            singleStationRes.printAll();
        }
    }

}
