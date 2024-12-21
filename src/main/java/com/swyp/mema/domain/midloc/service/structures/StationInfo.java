package com.swyp.mema.domain.midloc.service.structures;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StationInfo {

    String stationName;
    String line;
    @Setter
    int endTime;
    String lat;
    String lot;
    List<NextStation> nextStations;
    List<TransferStation> transferStationStations;

    @Builder
    public StationInfo(String stationName, String line, String lat, String lot) {
        this.stationName = cleanStationName(stationName);
        this.line = cleanLine(line);
        this.lat = lat;
        this.lot = lot;
        this.nextStations = new ArrayList<>();
        this.transferStationStations = new ArrayList<>();
    }

    /**
     * 역 명 파싱
     */
    private String cleanStationName(String stationName) {
        if (stationName == null) return null;
        return stationName
                .replaceAll("\\(.*?\\)", "") // 괄호와 괄호 안의 내용 전체 제거
//                .replaceAll("역$", "") // 마지막에 '역'이 있으면 제거     --> 이건 일단 냅두자
                .trim(); // 앞뒤 공백 제거
    }

    private String cleanLine(String line) {
        if (line == null) return null;
        String cleanedLine = line
                .replaceAll("\\(.*?\\)", "") // 괄호와 괄호 안의 내용 전체 제거
                .trim(); // 앞뒤 공백 제거

        //수인선, 분당선 통합
        if ("수인선".equals(cleanedLine) || "분당선".equals(cleanedLine)) {
            return "수인분당선";
        }
        return cleanedLine;
    }


    /**
     * 다음역 정보 추가
     */
    public void addNext(NextStation nextStation) {
        nextStations.add(nextStation);
    }

    /**
     * 환승역 정보 추가
     */
    public void addTransfer(TransferStation transferStation) {
        transferStationStations.add(transferStation);
    }

    /**
     * stationInfo 출력
     */
    public void printInfo() {
        System.out.println("\tline: " + line + ", Station: " + stationName + ", End Time: " + endTime + ", lat: " + lat + ", lot: " + lot);
        for (NextStation nextStation : nextStations) {
            nextStation.printData();
        }
    }


}
