package com.swyp.mema.domain.midloc.service;

import com.swyp.mema.domain.midloc.service.structures.NextStation;
import com.swyp.mema.domain.midloc.service.structures.StationInfo;
import com.swyp.mema.domain.midloc.service.structures.TransferStation;
import com.swyp.mema.domain.station.dto.response.nearSubway.NearSubwayResponse;
import com.swyp.mema.domain.station.dto.response.nearSubway.TotalNearSubwayResponse;
import com.swyp.mema.domain.station.dto.response.subwayInfo.SingleStationResponse;
import com.swyp.mema.domain.station.service.NearStationService;
import com.swyp.mema.domain.station.service.StationService;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class Stations {

    @Getter
    private final HashMap<String, StationInfo> stationInfos;   // code로 station info를 조회하는 해시맵
    private final HashMap<String, String> realTIme_lineMap;   //실시간 line -> 라인
    private final HashMap<String, String> realTime_codeMap;   //실시간 idx(StatnId) -> 역코드

    private final StationService stationService;
    private final NearStationService nearStationService;

    public Stations(StationService stationService, NearStationService nearStationService) {
        this.stationInfos = new HashMap<>();
        this.realTIme_lineMap = new HashMap<>();
        this.realTime_codeMap = new HashMap<>();
        this.stationService = stationService;
        this.nearStationService = nearStationService;
    }

    @PostConstruct
    public void init() {
        System.out.println("Initializing Stations bean...");
        start();
    }

    public void addLine6() {
        stationInfos.get("6호선_응암").setEndTime(0);
        stationInfos.get("6호선_응암").addNext(
                NextStation.builder()
                        .stationCode("6호선_새절")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("6호선_새절").setEndTime(2);
        stationInfos.get("6호선_새절").addNext(
                NextStation.builder()
                        .stationCode("6호선_응암")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("6호선_새절").addNext(
                NextStation.builder()
                        .stationCode("6호선_증산")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("6호선_증산").setEndTime(4);
        stationInfos.get("6호선_증산").addNext(
                NextStation.builder()
                        .stationCode("6호선_새절")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("6호선_증산").addNext(
                NextStation.builder()
                        .stationCode("6호선_디지털미디어시티")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("6호선_디지털미디어시티").setEndTime(6);
        stationInfos.get("6호선_디지털미디어시티").addNext(
                NextStation.builder()
                        .stationCode("6호선_증산")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("6호선_디지털미디어시티").addNext(
                NextStation.builder()
                        .stationCode("6호선_월드컵경기장")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("6호선_월드컵경기장").setEndTime(8);
        stationInfos.get("6호선_월드컵경기장").addNext(
                NextStation.builder()
                        .stationCode("6호선_디지털미디어시티")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("6호선_월드컵경기장").addNext(
                NextStation.builder()
                        .stationCode("6호선_마포구청")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("6호선_마포구청").setEndTime(10);
        stationInfos.get("6호선_마포구청").addNext(
                NextStation.builder()
                        .stationCode("6호선_월드컵경기장")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("6호선_마포구청").addNext(
                NextStation.builder()
                        .stationCode("6호선_망원")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("6호선_망원").setEndTime(12);
        stationInfos.get("6호선_망원").addNext(
                NextStation.builder()
                        .stationCode("6호선_마포구청")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("6호선_망원").addNext(
                NextStation.builder()
                        .stationCode("6호선_합정")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("6호선_합정").setEndTime(14);
        stationInfos.get("6호선_합정").addNext(
                NextStation.builder()
                        .stationCode("6호선_망원")
                        .waitTime(2)
                        .build()
        );


    }
    public void addLine4() {

        // 4호선 역 정보 초기화
        stationInfos.get("4호선_진접").setEndTime(0);
        stationInfos.get("4호선_진접").addNext(
                NextStation.builder()
                        .stationCode("4호선_오남")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_오남").setEndTime(2);
        stationInfos.get("4호선_오남").addNext(
                NextStation.builder()
                        .stationCode("4호선_진접")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_오남").addNext(
                NextStation.builder()
                        .stationCode("4호선_별내별가람")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_별내별가람").setEndTime(4);
        stationInfos.get("4호선_별내별가람").addNext(
                NextStation.builder()
                        .stationCode("4호선_오남")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_별내별가람").addNext(
                NextStation.builder()
                        .stationCode("4호선_당고개")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_당고개").setEndTime(6);
        stationInfos.get("4호선_당고개").addNext(
                NextStation.builder()
                        .stationCode("4호선_별내별가람")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_당고개").addNext(
                NextStation.builder()
                        .stationCode("4호선_상계")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_상계").setEndTime(8);
        stationInfos.get("4호선_상계").addNext(
                NextStation.builder()
                        .stationCode("4호선_당고개")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_상계").addNext(
                NextStation.builder()
                        .stationCode("4호선_노원")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_노원").setEndTime(10);
        stationInfos.get("4호선_노원").addNext(
                NextStation.builder()
                        .stationCode("4호선_상계")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_노원").addNext(
                NextStation.builder()
                        .stationCode("4호선_창동")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_창동").setEndTime(12);
        stationInfos.get("4호선_창동").addNext(
                NextStation.builder()
                        .stationCode("4호선_노원")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_창동").addNext(
                NextStation.builder()
                        .stationCode("4호선_쌍문")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_쌍문").setEndTime(14);
        stationInfos.get("4호선_쌍문").addNext(
                NextStation.builder()
                        .stationCode("4호선_창동")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_쌍문").addNext(
                NextStation.builder()
                        .stationCode("4호선_수유")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_수유").setEndTime(16);
        stationInfos.get("4호선_수유").addNext(
                NextStation.builder()
                        .stationCode("4호선_쌍문")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_수유").addNext(
                NextStation.builder()
                        .stationCode("4호선_미아")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_미아").setEndTime(18);
        stationInfos.get("4호선_미아").addNext(
                NextStation.builder()
                        .stationCode("4호선_수유")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_미아").addNext(
                NextStation.builder()
                        .stationCode("4호선_미아사거리")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_미아사거리").setEndTime(20);
        stationInfos.get("4호선_미아사거리").addNext(
                NextStation.builder()
                        .stationCode("4호선_미아")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_미아사거리").addNext(
                NextStation.builder()
                        .stationCode("4호선_길음")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_길음").setEndTime(22);
        stationInfos.get("4호선_길음").addNext(
                NextStation.builder()
                        .stationCode("4호선_미아사거리")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_길음").addNext(
                NextStation.builder()
                        .stationCode("4호선_성신여대입구")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_성신여대입구").setEndTime(24);
        stationInfos.get("4호선_성신여대입구").addNext(
                NextStation.builder()
                        .stationCode("4호선_길음")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_성신여대입구").addNext(
                NextStation.builder()
                        .stationCode("4호선_한성대입구")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_한성대입구").setEndTime(26);
        stationInfos.get("4호선_한성대입구").addNext(
                NextStation.builder()
                        .stationCode("4호선_성신여대입구")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_한성대입구").addNext(
                NextStation.builder()
                        .stationCode("4호선_혜화")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_혜화").setEndTime(28);
        stationInfos.get("4호선_혜화").addNext(
                NextStation.builder()
                        .stationCode("4호선_한성대입구")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_혜화").addNext(
                NextStation.builder()
                        .stationCode("4호선_동대문")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_동대문").setEndTime(30);
        stationInfos.get("4호선_동대문").addNext(
                NextStation.builder()
                        .stationCode("4호선_혜화")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_동대문").addNext(
                NextStation.builder()
                        .stationCode("4호선_동대문역사문화공원")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_동대문역사문화공원").setEndTime(32);
        stationInfos.get("4호선_동대문역사문화공원").addNext(
                NextStation.builder()
                        .stationCode("4호선_동대문")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_동대문역사문화공원").addNext(
                NextStation.builder()
                        .stationCode("4호선_충무로")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_충무로").setEndTime(34);
        stationInfos.get("4호선_충무로").addNext(
                NextStation.builder()
                        .stationCode("4호선_동대문역사문화공원")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_충무로").addNext(
                NextStation.builder()
                        .stationCode("4호선_명동")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_명동").setEndTime(36);
        stationInfos.get("4호선_명동").addNext(
                NextStation.builder()
                        .stationCode("4호선_충무로")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_명동").addNext(
                NextStation.builder()
                        .stationCode("4호선_회현")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_회현").setEndTime(38);
        stationInfos.get("4호선_회현").addNext(
                NextStation.builder()
                        .stationCode("4호선_명동")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_회현").addNext(
                NextStation.builder()
                        .stationCode("4호선_서울역")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_서울역").setEndTime(40);
        stationInfos.get("4호선_서울역").addNext(
                NextStation.builder()
                        .stationCode("4호선_회현")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_서울역").addNext(
                NextStation.builder()
                        .stationCode("4호선_숙대입구")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_숙대입구").setEndTime(42);
        stationInfos.get("4호선_숙대입구").addNext(
                NextStation.builder()
                        .stationCode("4호선_서울역")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_숙대입구").addNext(
                NextStation.builder()
                        .stationCode("4호선_삼각지")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_삼각지").setEndTime(44);
        stationInfos.get("4호선_삼각지").addNext(
                NextStation.builder()
                        .stationCode("4호선_숙대입구")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_삼각지").addNext(
                NextStation.builder()
                        .stationCode("4호선_신용산")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_신용산").setEndTime(46);
        stationInfos.get("4호선_신용산").addNext(
                NextStation.builder()
                        .stationCode("4호선_삼각지")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_신용산").addNext(
                NextStation.builder()
                        .stationCode("4호선_이촌")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_이촌").setEndTime(48);
        stationInfos.get("4호선_이촌").addNext(
                NextStation.builder()
                        .stationCode("4호선_신용산")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_이촌").addNext(
                NextStation.builder()
                        .stationCode("4호선_동작(현충원)")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_동작").setEndTime(50);
        stationInfos.get("4호선_동작").addNext(
                NextStation.builder()
                        .stationCode("4호선_이촌")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_동작").addNext(
                NextStation.builder()
                        .stationCode("4호선_총신대입구")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_총신대입구").setEndTime(52);
        stationInfos.get("4호선_총신대입구").addNext(
                NextStation.builder()
                        .stationCode("4호선_동작")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_총신대입구").addNext(
                NextStation.builder()
                        .stationCode("4호선_사당")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_사당").setEndTime(54);
        stationInfos.get("4호선_사당").addNext(
                NextStation.builder()
                        .stationCode("4호선_총신대입구")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_사당").addNext(
                NextStation.builder()
                        .stationCode("4호선_남태령")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_남태령").setEndTime(56);
        stationInfos.get("4호선_남태령").addNext(
                NextStation.builder()
                        .stationCode("4호선_사당")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_남태령").addNext(
                NextStation.builder()
                        .stationCode("4호선_선바위")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_선바위").setEndTime(58);
        stationInfos.get("4호선_선바위").addNext(
                NextStation.builder()
                        .stationCode("4호선_남태령")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_선바위").addNext(
                NextStation.builder()
                        .stationCode("4호선_경마공원")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_경마공원").setEndTime(60);
        stationInfos.get("4호선_경마공원").addNext(
                NextStation.builder()
                        .stationCode("4호선_선바위")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_경마공원").addNext(
                NextStation.builder()
                        .stationCode("4호선_대공원")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_대공원").setEndTime(62);
        stationInfos.get("4호선_대공원").addNext(
                NextStation.builder()
                        .stationCode("4호선_경마공원")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_대공원").addNext(
                NextStation.builder()
                        .stationCode("4호선_과천")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_과천").setEndTime(64);
        stationInfos.get("4호선_과천").addNext(
                NextStation.builder()
                        .stationCode("4호선_대공원")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_과천").addNext(
                NextStation.builder()
                        .stationCode("4호선_정부과천청사")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_정부과천청사").setEndTime(66);
        stationInfos.get("4호선_정부과천청사").addNext(
                NextStation.builder()
                        .stationCode("4호선_과천")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_인덕원").setEndTime(68);
        stationInfos.get("4호선_인덕원").addNext(
                NextStation.builder()
                        .stationCode("4호선_정부과천청사")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_인덕원").addNext(
                NextStation.builder()
                        .stationCode("4호선_평촌")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_평촌").setEndTime(70);
        stationInfos.get("4호선_평촌").addNext(
                NextStation.builder()
                        .stationCode("4호선_인덕원")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_평촌").addNext(
                NextStation.builder()
                        .stationCode("4호선_범계")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_범계").setEndTime(72);
        stationInfos.get("4호선_범계").addNext(
                NextStation.builder()
                        .stationCode("4호선_평촌")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_범계").addNext(
                NextStation.builder()
                        .stationCode("4호선_금정")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_금정").setEndTime(74);
        stationInfos.get("4호선_금정").addNext(
                NextStation.builder()
                        .stationCode("4호선_범계")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_금정").addNext(
                NextStation.builder()
                        .stationCode("4호선_산본")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_산본").setEndTime(76);
        stationInfos.get("4호선_산본").addNext(
                NextStation.builder()
                        .stationCode("4호선_금정")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_산본").addNext(
                NextStation.builder()
                        .stationCode("4호선_수리산")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_수리산").setEndTime(78);
        stationInfos.get("4호선_수리산").addNext(
                NextStation.builder()
                        .stationCode("4호선_산본")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_수리산").addNext(
                NextStation.builder()
                        .stationCode("4호선_대야미")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_대야미").setEndTime(80);
        stationInfos.get("4호선_대야미").addNext(
                NextStation.builder()
                        .stationCode("4호선_수리산")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_대야미").addNext(
                NextStation.builder()
                        .stationCode("4호선_반월")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_반월").setEndTime(82);
        stationInfos.get("4호선_반월").addNext(
                NextStation.builder()
                        .stationCode("4호선_대야미")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_반월").addNext(
                NextStation.builder()
                        .stationCode("4호선_상록수")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_상록수").setEndTime(84);
        stationInfos.get("4호선_상록수").addNext(
                NextStation.builder()
                        .stationCode("4호선_반월")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_상록수").addNext(
                NextStation.builder()
                        .stationCode("4호선_한대앞")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_한대앞").setEndTime(86);
        stationInfos.get("4호선_한대앞").addNext(
                NextStation.builder()
                        .stationCode("4호선_상록수")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_한대앞").addNext(
                NextStation.builder()
                        .stationCode("4호선_중앙")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_중앙").setEndTime(88);
        stationInfos.get("4호선_중앙").addNext(
                NextStation.builder()
                        .stationCode("4호선_한대앞")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_중앙").addNext(
                NextStation.builder()
                        .stationCode("4호선_고잔")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_고잔").setEndTime(90);
        stationInfos.get("4호선_고잔").addNext(
                NextStation.builder()
                        .stationCode("4호선_중앙")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_고잔").addNext(
                NextStation.builder()
                        .stationCode("4호선_초지")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_초지").setEndTime(92);
        stationInfos.get("4호선_초지").addNext(
                NextStation.builder()
                        .stationCode("4호선_고잔")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_초지").addNext(
                NextStation.builder()
                        .stationCode("4호선_안산")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_안산").setEndTime(94);
        stationInfos.get("4호선_안산").addNext(
                NextStation.builder()
                        .stationCode("4호선_초지")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_안산").addNext(
                NextStation.builder()
                        .stationCode("4호선_신길온천")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_신길온천").setEndTime(96);
        stationInfos.get("4호선_신길온천").addNext(
                NextStation.builder()
                        .stationCode("4호선_안산")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_신길온천").addNext(
                NextStation.builder()
                        .stationCode("4호선_정왕")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_정왕").setEndTime(98);
        stationInfos.get("4호선_정왕").addNext(
                NextStation.builder()
                        .stationCode("4호선_신길온천")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("4호선_정왕").addNext(
                NextStation.builder()
                        .stationCode("4호선_오이도")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("4호선_오이도").setEndTime(100);
        stationInfos.get("4호선_오이도").addNext(
                NextStation.builder()
                        .stationCode("4호선_정왕")
                        .waitTime(2)
                        .build()
        );
    }
    public void addLine3(){

        // 3호선 역 정보 초기화
        stationInfos.get("3호선_대화").setEndTime(0);
        stationInfos.get("3호선_대화").addNext(
                NextStation.builder()
                        .stationCode("3호선_주엽")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_대화").addNext(
                NextStation.builder()
                        .stationCode("3호선_정발산")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_주엽").setEndTime(2);
        stationInfos.get("3호선_주엽").addNext(
                NextStation.builder()
                        .stationCode("3호선_대화")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_주엽").addNext(
                NextStation.builder()
                        .stationCode("3호선_정발산")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_정발산").setEndTime(4);
        stationInfos.get("3호선_정발산").addNext(
                NextStation.builder()
                        .stationCode("3호선_주엽")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_정발산").addNext(
                NextStation.builder()
                        .stationCode("3호선_마두")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_마두").setEndTime(6);
        stationInfos.get("3호선_마두").addNext(
                NextStation.builder()
                        .stationCode("3호선_정발산")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_마두").addNext(
                NextStation.builder()
                        .stationCode("3호선_백석")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_백석").setEndTime(8);
        stationInfos.get("3호선_백석").addNext(
                NextStation.builder()
                        .stationCode("3호선_마두")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_백석").addNext(
                NextStation.builder()
                        .stationCode("3호선_대곡")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_대곡").setEndTime(10);
        stationInfos.get("3호선_대곡").addNext(
                NextStation.builder()
                        .stationCode("3호선_백석")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_대곡").addNext(
                NextStation.builder()
                        .stationCode("3호선_화정")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_화정").setEndTime(12);
        stationInfos.get("3호선_화정").addNext(
                NextStation.builder()
                        .stationCode("3호선_대곡")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_화정").addNext(
                NextStation.builder()
                        .stationCode("3호선_원당")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_원당").setEndTime(14);
        stationInfos.get("3호선_원당").addNext(
                NextStation.builder()
                        .stationCode("3호선_화정")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_원당").addNext(
                NextStation.builder()
                        .stationCode("3호선_삼송")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_삼송").setEndTime(16);
        stationInfos.get("3호선_삼송").addNext(
                NextStation.builder()
                        .stationCode("3호선_원당")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_삼송").addNext(
                NextStation.builder()
                        .stationCode("3호선_지축")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_지축").setEndTime(18);
        stationInfos.get("3호선_지축").addNext(
                NextStation.builder()
                        .stationCode("3호선_삼송")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_지축").addNext(
                NextStation.builder()
                        .stationCode("3호선_구파발")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_구파발").setEndTime(20);
        stationInfos.get("3호선_구파발").addNext(
                NextStation.builder()
                        .stationCode("3호선_지축")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_구파발").addNext(
                NextStation.builder()
                        .stationCode("3호선_연신내")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_연신내").setEndTime(22);
        stationInfos.get("3호선_연신내").addNext(
                NextStation.builder()
                        .stationCode("3호선_구파발")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_연신내").addNext(
                NextStation.builder()
                        .stationCode("3호선_불광")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_불광").setEndTime(24);
        stationInfos.get("3호선_불광").addNext(
                NextStation.builder()
                        .stationCode("3호선_연신내")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_불광").addNext(
                NextStation.builder()
                        .stationCode("3호선_녹번")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_녹번").setEndTime(26);
        stationInfos.get("3호선_녹번").addNext(
                NextStation.builder()
                        .stationCode("3호선_불광")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_녹번").addNext(
                NextStation.builder()
                        .stationCode("3호선_홍제")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_홍제").setEndTime(28);
        stationInfos.get("3호선_홍제").addNext(
                NextStation.builder()
                        .stationCode("3호선_녹번")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_홍제").addNext(
                NextStation.builder()
                        .stationCode("3호선_무악재")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_무악재").setEndTime(30);
        stationInfos.get("3호선_무악재").addNext(
                NextStation.builder()
                        .stationCode("3호선_홍제")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_무악재").addNext(
                NextStation.builder()
                        .stationCode("3호선_독립문")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_독립문").setEndTime(32);
        stationInfos.get("3호선_독립문").addNext(
                NextStation.builder()
                        .stationCode("3호선_무악재")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_독립문").addNext(
                NextStation.builder()
                        .stationCode("3호선_경복궁")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_경복궁").setEndTime(34);
        stationInfos.get("3호선_경복궁").addNext(
                NextStation.builder()
                        .stationCode("3호선_독립문")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_경복궁").addNext(
                NextStation.builder()
                        .stationCode("3호선_안국")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_안국").setEndTime(36);
        stationInfos.get("3호선_안국").addNext(
                NextStation.builder()
                        .stationCode("3호선_경복궁")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_안국").addNext(
                NextStation.builder()
                        .stationCode("3호선_종로3가")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_종로3가").setEndTime(38);
        stationInfos.get("3호선_종로3가").addNext(
                NextStation.builder()
                        .stationCode("3호선_안국")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_종로3가").addNext(
                NextStation.builder()
                        .stationCode("3호선_을지로3가")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_을지로3가").setEndTime(40);
        stationInfos.get("3호선_을지로3가").addNext(
                NextStation.builder()
                        .stationCode("3호선_종로3가")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_을지로3가").addNext(
                NextStation.builder()
                        .stationCode("3호선_충무로")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_충무로").setEndTime(42);
        stationInfos.get("3호선_충무로").addNext(
                NextStation.builder()
                        .stationCode("3호선_을지로3가")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_충무로").addNext(
                NextStation.builder()
                        .stationCode("3호선_동대입구")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_동대입구").setEndTime(44);
        stationInfos.get("3호선_동대입구").addNext(
                NextStation.builder()
                        .stationCode("3호선_충무로")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_동대입구").addNext(
                NextStation.builder()
                        .stationCode("3호선_약수")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_약수").setEndTime(46);
        stationInfos.get("3호선_약수").addNext(
                NextStation.builder()
                        .stationCode("3호선_동대입구")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_약수").addNext(
                NextStation.builder()
                        .stationCode("3호선_금호")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_금호").setEndTime(48);
        stationInfos.get("3호선_금호").addNext(
                NextStation.builder()
                        .stationCode("3호선_약수")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_금호").addNext(
                NextStation.builder()
                        .stationCode("3호선_옥수")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_옥수").setEndTime(50);
        stationInfos.get("3호선_옥수").addNext(
                NextStation.builder()
                        .stationCode("3호선_금호")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_옥수").addNext(
                NextStation.builder()
                        .stationCode("3호선_압구정")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_압구정").setEndTime(52);
        stationInfos.get("3호선_압구정").addNext(
                NextStation.builder()
                        .stationCode("3호선_옥수")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_압구정").addNext(
                NextStation.builder()
                        .stationCode("3호선_신사")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_신사").setEndTime(54);
        stationInfos.get("3호선_신사").addNext(
                NextStation.builder()
                        .stationCode("3호선_압구정")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_신사").addNext(
                NextStation.builder()
                        .stationCode("3호선_잠원")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_잠원").setEndTime(56);
        stationInfos.get("3호선_잠원").addNext(
                NextStation.builder()
                        .stationCode("3호선_신사")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_잠원").addNext(
                NextStation.builder()
                        .stationCode("3호선_고속터미널")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_고속터미널").setEndTime(58);
        stationInfos.get("3호선_고속터미널").addNext(
                NextStation.builder()
                        .stationCode("3호선_잠원")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_고속터미널").addNext(
                NextStation.builder()
                        .stationCode("3호선_교대")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_교대").setEndTime(60);
        stationInfos.get("3호선_교대").addNext(
                NextStation.builder()
                        .stationCode("3호선_고속터미널")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_교대").addNext(
                NextStation.builder()
                        .stationCode("3호선_남부터미널")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_남부터미널").setEndTime(62);
        stationInfos.get("3호선_남부터미널").addNext(
                NextStation.builder()
                        .stationCode("3호선_교대")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_남부터미널").addNext(
                NextStation.builder()
                        .stationCode("3호선_양재")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_양재").setEndTime(64);
        stationInfos.get("3호선_양재").addNext(
                NextStation.builder()
                        .stationCode("3호선_남부터미널")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_양재").addNext(
                NextStation.builder()
                        .stationCode("3호선_매봉")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_매봉").setEndTime(66);
        stationInfos.get("3호선_매봉").addNext(
                NextStation.builder()
                        .stationCode("3호선_양재")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_매봉").addNext(
                NextStation.builder()
                        .stationCode("3호선_도곡")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_도곡").setEndTime(68);
        stationInfos.get("3호선_도곡").addNext(
                NextStation.builder()
                        .stationCode("3호선_매봉")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_도곡").addNext(
                NextStation.builder()
                        .stationCode("3호선_대치")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_대치").setEndTime(70);
        stationInfos.get("3호선_대치").addNext(
                NextStation.builder()
                        .stationCode("3호선_도곡")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_대치").addNext(
                NextStation.builder()
                        .stationCode("3호선_학여울")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_학여울").setEndTime(72);
        stationInfos.get("3호선_학여울").addNext(
                NextStation.builder()
                        .stationCode("3호선_대치")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_학여울").addNext(
                NextStation.builder()
                        .stationCode("3호선_대청")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_대청").setEndTime(74);
        stationInfos.get("3호선_대청").addNext(
                NextStation.builder()
                        .stationCode("3호선_학여울")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_대청").addNext(
                NextStation.builder()
                        .stationCode("3호선_일원")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_일원").setEndTime(76);
        stationInfos.get("3호선_일원").addNext(
                NextStation.builder()
                        .stationCode("3호선_대청")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_일원").addNext(
                NextStation.builder()
                        .stationCode("3호선_수서")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_수서").setEndTime(78);
        stationInfos.get("3호선_수서").addNext(
                NextStation.builder()
                        .stationCode("3호선_일원")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_수서").addNext(
                NextStation.builder()
                        .stationCode("3호선_가락시장")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_가락시장").setEndTime(80);
        stationInfos.get("3호선_가락시장").addNext(
                NextStation.builder()
                        .stationCode("3호선_수서")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_가락시장").addNext(
                NextStation.builder()
                        .stationCode("3호선_경찰병원")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_경찰병원").setEndTime(82);
        stationInfos.get("3호선_경찰병원").addNext(
                NextStation.builder()
                        .stationCode("3호선_가락시장")
                        .waitTime(2)
                        .build()
        );
        stationInfos.get("3호선_경찰병원").addNext(
                NextStation.builder()
                        .stationCode("3호선_오금")
                        .waitTime(2)
                        .build()
        );

        stationInfos.get("3호선_오금").setEndTime(84);
        stationInfos.get("3호선_오금").addNext(
                NextStation.builder()
                        .stationCode("3호선_경찰병원")
                        .waitTime(2)
                        .build()
        );
    }
    public void addTransfers(){

        //line 1
        // 서울역 (1호선, 4호선)
        stationInfos.get("1호선_서울역").getTransferStationStations().add(
                TransferStation.builder().stationCode("4호선_서울역").transferTime(5).build()
        );

        // 시청 (1호선, 2호선)
        stationInfos.get("1호선_시청").getTransferStationStations().add(
                TransferStation.builder().stationCode("2호선_시청").transferTime(3).build()
        );

        // 종로3가 (1호선, 3호선, 5호선)
        stationInfos.get("1호선_종로3가").getTransferStationStations().add(
                TransferStation.builder().stationCode("3호선_종로3가").transferTime(5).build()
        );
        stationInfos.get("1호선_종로3가").getTransferStationStations().add(
                TransferStation.builder().stationCode("5호선_종로3가").transferTime(5).build()
        );

        // 동대문 (1호선, 4호선)
        stationInfos.get("1호선_동대문").getTransferStationStations().add(
                TransferStation.builder().stationCode("4호선_동대문").transferTime(4).build()
        );

        // 신설동 (1호선, 2호선 성수지선)
        stationInfos.get("1호선_신설동").getTransferStationStations().add(
                TransferStation.builder().stationCode("2호선_신설동").transferTime(3).build()
        );

        // 신도림 (1호선, 2호선)
        stationInfos.get("1호선_신도림").getTransferStationStations().add(
                TransferStation.builder().stationCode("2호선_신도림").transferTime(5).build()
        );

        // 노량진 (1호선, 9호선)
        stationInfos.get("1호선_노량진").getTransferStationStations().add(
                TransferStation.builder().stationCode("9호선_노량진").transferTime(6).build()
        );

        // 신길 (1호선, 5호선)
        stationInfos.get("1호선_신길").getTransferStationStations().add(
                TransferStation.builder().stationCode("5호선_신길").transferTime(4).build()
        );

        // 가산디지털단지 (1호선, 7호선)
        stationInfos.get("1호선_가산디지털단지").getTransferStationStations().add(
                TransferStation.builder().stationCode("7호선_가산디지털단지").transferTime(7).build()
        );

        // 금정 (1호선, 4호선)
        stationInfos.get("1호선_금정").getTransferStationStations().add(
                TransferStation.builder().stationCode("4호선_금정").transferTime(5).build()
        );

        //line2
        stationInfos.get("2호선_시청").getTransferStationStations().add(
                TransferStation.builder().stationCode("1호선_시청").transferTime(3).build()
        );

        stationInfos.get("2호선_을지로3가").getTransferStationStations().add(
                TransferStation.builder().stationCode("3호선_을지로3가").transferTime(5).build()
        );

        stationInfos.get("2호선_을지로4가").getTransferStationStations().add(
                TransferStation.builder().stationCode("5호선_을지로4가").transferTime(5).build()
        );

        stationInfos.get("2호선_동대문역사문화공원").getTransferStationStations().add(
                TransferStation.builder().stationCode("4호선_동대문역사문화공원").transferTime(5).build()
        );
        stationInfos.get("2호선_동대문역사문화공원").getTransferStationStations().add(
                TransferStation.builder().stationCode("5호선_동대문역사문화공원").transferTime(5).build()
        );

        stationInfos.get("2호선_신당").getTransferStationStations().add(
                TransferStation.builder().stationCode("6호선_신당").transferTime(5).build()
        );

        stationInfos.get("2호선_왕십리").getTransferStationStations().add(
                TransferStation.builder().stationCode("5호선_왕십리").transferTime(5).build()
        );

        stationInfos.get("2호선_건대입구").getTransferStationStations().add(
                TransferStation.builder().stationCode("7호선_건대입구").transferTime(5).build()
        );

        stationInfos.get("2호선_잠실").getTransferStationStations().add(
                TransferStation.builder().stationCode("8호선_잠실").transferTime(5).build()
        );

        stationInfos.get("2호선_종합운동장").getTransferStationStations().add(
                TransferStation.builder().stationCode("9호선_종합운동장").transferTime(5).build()
        );

        stationInfos.get("2호선_교대").getTransferStationStations().add(
                TransferStation.builder().stationCode("3호선_교대").transferTime(5).build()
        );

        stationInfos.get("2호선_사당").getTransferStationStations().add(
                TransferStation.builder().stationCode("4호선_사당").transferTime(5).build()
        );

        stationInfos.get("2호선_대림").getTransferStationStations().add(
                TransferStation.builder().stationCode("7호선_대림").transferTime(5).build()
        );

        //line 3
        stationInfos.get("3호선_지총").addTransfer(TransferStation.builder().stationCode("6호선_지총").transferTime(4).build());
        stationInfos.get("3호선_연신내").addTransfer(TransferStation.builder().stationCode("6호선_연신내").transferTime(5).build());
        stationInfos.get("3호선_불광").addTransfer(TransferStation.builder().stationCode("6호선_불광").transferTime(4).build());
        stationInfos.get("3호선_도림문").addTransfer(TransferStation.builder().stationCode("5호선_도림문").transferTime(3).build());
        stationInfos.get("3호선_경복궁").addTransfer(TransferStation.builder().stationCode("5호선_경복궁").transferTime(4).build());
        stationInfos.get("3호선_안국").addTransfer(TransferStation.builder().stationCode("5호선_안국").transferTime(4).build());
        stationInfos.get("3호선_종로3가").addTransfer(TransferStation.builder().stationCode("1호선_종로3가").transferTime(6).build());
        stationInfos.get("3호선_종로3가").addTransfer(TransferStation.builder().stationCode("5호선_종로3가").transferTime(5).build());
        stationInfos.get("3호선_을지로3가").addTransfer(TransferStation.builder().stationCode("2호선_을지로3가").transferTime(4).build());
        stationInfos.get("3호선_췚무로").addTransfer(TransferStation.builder().stationCode("4호선_췚무로").transferTime(4).build());
        stationInfos.get("3호선_동대입구").addTransfer(TransferStation.builder().stationCode("5호선_동대입구").transferTime(3).build());
        stationInfos.get("3호선_약수").addTransfer(TransferStation.builder().stationCode("6호선_약수").transferTime(4).build());
        stationInfos.get("3호선_금호").addTransfer(TransferStation.builder().stationCode("5호선_금호").transferTime(4).build());
        stationInfos.get("3호선_고속터미널").addTransfer(TransferStation.builder().stationCode("7호선_고속터미널").transferTime(5).build());
        stationInfos.get("3호선_고속터미널").addTransfer(TransferStation.builder().stationCode("9호선_고속터미널").transferTime(5).build());
        stationInfos.get("3호선_교대").addTransfer(TransferStation.builder().stationCode("2호선_교대").transferTime(4).build());
        stationInfos.get("3호선_남부터미널").addTransfer(TransferStation.builder().stationCode("2호선_남부터미널").transferTime(3).build());
        stationInfos.get("3호선_가락시장").addTransfer(TransferStation.builder().stationCode("8호선_가락시장").transferTime(4).build());
        stationInfos.get("3호선_오금").addTransfer(TransferStation.builder().stationCode("5호선_오금").transferTime(4).build());


        //line 4
        stationInfos.get("4호선_창동").addTransfer(TransferStation.builder().stationCode("1호선_창동").transferTime(4).build());
        stationInfos.get("4호선_동대문").addTransfer(TransferStation.builder().stationCode("1호선_동대문").transferTime(3).build());
        stationInfos.get("4호선_동대문역사문화공원").addTransfer(TransferStation.builder().stationCode("2호선_동대문역사문화공원").transferTime(5).build());
        stationInfos.get("4호선_동대문역사문화공원").addTransfer(TransferStation.builder().stationCode("5호선_동대문역사문화공원").transferTime(6).build());
        stationInfos.get("4호선_충무로").addTransfer(TransferStation.builder().stationCode("3호선_충무로").transferTime(4).build());
        stationInfos.get("4호선_서울역").addTransfer(TransferStation.builder().stationCode("1호선_서울역").transferTime(5).build());
        stationInfos.get("4호선_삼각지").addTransfer(TransferStation.builder().stationCode("6호선_삼각지").transferTime(4).build());
        stationInfos.get("4호선_동작").addTransfer(TransferStation.builder().stationCode("9호선_동작").transferTime(5).build());
        stationInfos.get("4호선_총신대입구").addTransfer(TransferStation.builder().stationCode("7호선_총신대입구").transferTime(4).build());
        stationInfos.get("4호선_사당").addTransfer(TransferStation.builder().stationCode("2호선_사당").transferTime(4).build());
        stationInfos.get("4호선_노원").addTransfer(TransferStation.builder().stationCode("7호선_노원").transferTime(4).build());
        stationInfos.get("4호선_금정").addTransfer(TransferStation.builder().stationCode("1호선_금정").transferTime(4).build());


        //line 5
        stationInfos.get("5호선_동대문역사문화공원").addTransfer(TransferStation.builder().stationCode("2호선_동대문역사문화공원").transferTime(5).build());
        stationInfos.get("5호선_동대문역사문화공원").addTransfer(TransferStation.builder().stationCode("4호선_동대문역사문화공원").transferTime(6).build());
        stationInfos.get("5호선_종로3가").addTransfer(TransferStation.builder().stationCode("1호선_종로3가").transferTime(6).build());
        stationInfos.get("5호선_종로3가").addTransfer(TransferStation.builder().stationCode("3호선_종로3가").transferTime(5).build());
        stationInfos.get("5호선_을지로4가").addTransfer(TransferStation.builder().stationCode("2호선_을지로4가").transferTime(4).build());
        stationInfos.get("5호선_충정로").addTransfer(TransferStation.builder().stationCode("2호선_충정로").transferTime(4).build());
        stationInfos.get("5호선_공덕").addTransfer(TransferStation.builder().stationCode("6호선_공덕").transferTime(4).build());
        stationInfos.get("5호선_여의도").addTransfer(TransferStation.builder().stationCode("9호선_여의도").transferTime(5).build());
        stationInfos.get("5호선_영등포구청").addTransfer(TransferStation.builder().stationCode("2호선_영등포구청").transferTime(4).build());
        stationInfos.get("5호선_까치산").addTransfer(TransferStation.builder().stationCode("2호선_까치산").transferTime(4).build());
        stationInfos.get("5호선_왕십리").addTransfer(TransferStation.builder().stationCode("2호선_왕십리").transferTime(5).build());
        stationInfos.get("5호선_군자").addTransfer(TransferStation.builder().stationCode("7호선_군자").transferTime(4).build());
        stationInfos.get("5호선_천호").addTransfer(TransferStation.builder().stationCode("8호선_천호").transferTime(5).build());
        stationInfos.get("5호선_오금").addTransfer(TransferStation.builder().stationCode("3호선_오금").transferTime(4).build());


        //line 6



    }


    public void start(){
        makeRealTimeLineMap();
        makeStations();
//        makeRealTimeCodeMaps();
//        setRelations();
    }

    /*
        stations에 stationName, line정보 생성
     */
    public void makeRealTimeLineMap(){

        // 노선 코드와 이름 추가
        realTIme_lineMap.put("1001", "1호선");
        realTIme_lineMap.put("1002", "2호선");
        realTIme_lineMap.put("1003", "3호선");
        realTIme_lineMap.put("1004", "4호선");
        realTIme_lineMap.put("1005", "5호선");
        realTIme_lineMap.put("1006", "6호선");
        realTIme_lineMap.put("1007", "7호선");
        realTIme_lineMap.put("1008", "8호선");
        realTIme_lineMap.put("1009", "9호선");
        realTIme_lineMap.put("1061", "중앙선");
        realTIme_lineMap.put("1063", "경의중앙선");
        realTIme_lineMap.put("1065", "공항철도");
        realTIme_lineMap.put("1067", "경춘선");
        realTIme_lineMap.put("1075", "수의분당선");
        realTIme_lineMap.put("1077", "신분당선");
        realTIme_lineMap.put("1092", "우이신설선");
        realTIme_lineMap.put("1093", "서해선");
        realTIme_lineMap.put("1081", "경강선");
        realTIme_lineMap.put("1032", "GTX-A");
    }

    /*
        실시간 지하철 정보 API의 역 코드를 index로 하는 hash map 생성
     */
    public void makeRealTimeCodeMaps(){

        for(StationInfo stationInfo : this.stationInfos.values()){

//            System.out.println(stationInfo.getLine() + " " + stationInfo.getStationName());
            TotalNearSubwayResponse reses = nearStationService.getNearSubwayByAPI(stationInfo.getStationName());
            if (reses == null) continue;
            makeRealTimeCodeMap(reses);
        }
    }

    public void makeRealTimeCodeMap(TotalNearSubwayResponse totalRes) {
        if (totalRes == null || totalRes.getSubwayTimeList() == null) {
            System.out.println("TotalNearSubwayResponse is null or its subwayTimeList is null.");
            return;
        }

        for (NearSubwayResponse res : totalRes.getSubwayTimeList()) {
            if (res == null || res.getStatnId() == null) {
                System.out.println("NearSubwayResponse or statnId is null.");
                continue;
            }

            if (realTime_codeMap.containsKey(res.getStatnId())) {
                continue;
            }

            String name = res.getStatnNm();
            String line = realTIme_lineMap.get(res.getSubwayLine());
            if (name == null || line == null) {
                System.out.println("Invalid station name or line.");
                continue;
            }

            String code = line + "_" + name;
            realTime_codeMap.put(res.getStatnId(), code);
        }
    }

    /*
        stationInfos에 역의 이름, 라인, 죄표 정보 세팅
     */
    public void makeStations() {

        List<SingleStationResponse> reses = stationService.getSubwayInfo().getStationList();

        for (SingleStationResponse res : reses) {
            String code = res.getRouteName() + "_" + res.getStationName();
            StationInfo stationInfo = makeStation(res);
            this.stationInfos.put(code, stationInfo);
            stationInfo.printInfo();
        }
    }

    /*
        단일 stationInfo 생성
     */
    public StationInfo makeStation(SingleStationResponse res) {

        StationInfo stationInfo = StationInfo.builder()
                .stationName(res.getStationName())
                .line(res.getRouteName())
                .lat(res.getLat())
                .lot(res.getLot())
                .build();
        return stationInfo;
    }

    /*
        모든 역에 대한 NearSubwayResponse를 호출하여 역 관계 세팅
     */
    public void setRelations(){

        for(StationInfo stationInfo : this.stationInfos.values()){

            TotalNearSubwayResponse reses = nearStationService.getNearSubwayByAPI(stationInfo.getStationName());
            if (reses == null) continue;
            for(NearSubwayResponse res : reses.getSubwayTimeList()){

                addNext(res);
                addTransfer(res);
            }
            stationInfo.printInfo();
        }
    }

    /*
        stationInfo에 다음역 정보 추가
     */
    public void addNext(NearSubwayResponse res) {


        String currentStationCode = (realTime_codeMap.get(res.getStatnId()));
        StationInfo curStationInfo = stationInfos.get(currentStationCode);

        //  다음역에 대해 NextStation 추가
        String nextId = res.getStatnTid();
        if (nextId != null) {

            String nextStationCode = realTime_codeMap.get(nextId);

            boolean flag = false;
            //  nextStationCode가 이미 next에 추가되었는지 체크 => 이미 있다면 true
            for (NextStation next : curStationInfo.getNextStations()) {

                if (Objects.equals(next.getStationCode(), nextStationCode)) {

                    flag = true;
                    break;
                }
            }

            if (!flag) {

                NextStation newNext = NextStation.builder()
                        .stationCode(nextStationCode)
                        .build();

                int waitTime = 5; //계산 함수 추가
                newNext.setWaitTime(waitTime);
                curStationInfo.addNext(newNext);
            }
        }

        //  이전역에 대해 NextStation 추가
        String prevId = res.getStatnFid();
        if (prevId != null) {

            String prevStationCode = realTime_codeMap.get(prevId);

            boolean flag = false;
            //  nextStationCode가 이미 next에 추가되었는지 체크 => 이미 있다면 true
            for (NextStation next : curStationInfo.getNextStations()) {

                if (Objects.equals(next.getStationCode(), prevStationCode)) {

                    flag = true;
                    break;
                }
            }

            if (!flag) {

                NextStation newNext = NextStation.builder()
                        .stationCode(prevStationCode)
                        .build();

                int waitTime = 5; //계산 함수 추가
                newNext.setWaitTime(waitTime);
                curStationInfo.addNext(newNext);
            }
        }
    }

    /*
        환승역 정보 추가
     */
    public void addTransfer(NearSubwayResponse res) {


        String currentStationCode = (realTime_codeMap.get(res.getStatnId()));
        StationInfo curStationInfo = stationInfos.get(currentStationCode);

        //  현재 역에 이미 환승역 정보가 들어가 있음
        if (!curStationInfo.getNextStations().isEmpty()) {

            return;
        }

        //  환승역 개수
        int transferNum = Integer.parseInt(res.getTrnsitCo());

        List<String> transferLists = new ArrayList<String>();
        if (transferNum != transferLists.size()) {

            System.out.println("something wrong in Transfer Data");
        }

        //  환승역 개수만큼 for문 돌림
        for (String transfer : transferLists) {

            //  환승역 코드 추출
            String transferCode = realTime_codeMap.get(transfer);

            int transferTime = 5;   //  환승시간 계산하는 함수 호출
            TransferStation newTransfer = TransferStation.builder()
                    .stationCode(transferCode)
                    .transferTime(transferTime)
                    .build();

            curStationInfo.addTransfer(newTransfer);
        }
    }




}
