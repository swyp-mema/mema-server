package com.swyp.mema.database.station.util;

import com.swyp.mema.database.station.model._Station;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StringCleaner {

    HashMap<String, List<String>> lines = new HashMap<>();
    HashMap<String, List<String>> stationNames = new HashMap<>();



    public StringCleaner() {
        setLines();
        setStationNames();
    }

    private void setLines(){

        lines.put("1호선", Arrays.asList("경부선", "경인선", "경원선"));
        lines.put("2호선", Arrays.asList("성수지선", "신도림지선"));
        lines.put("3호선", Arrays.asList("일산선"));
        lines.put("4호선", Arrays.asList("과천선", "안산선", "안산과천선", "진접선"));
        lines.put("7호선", Arrays.asList("도시철도7호선"));
        lines.put("8호선", Arrays.asList("수도권광역철도8호선", "별내선"));
        lines.put("9호선", Arrays.asList("수도권도시철도9호선", "서울도시철도9호선"));
        lines.put("공항철도", Arrays.asList("공항철도1호선", "공항철도선", "인천국제공항선", "공항"));
        lines.put("용인경전철", Arrays.asList("에버라인","에버라인선"));
        lines.put("수인분당선", Arrays.asList("수인선", "분당선", "수인분당"));
        lines.put("신분당선", Arrays.asList("신분당"));
        lines.put("경의중앙선", Arrays.asList("중앙선", "경의중앙", "경의선", "경의·중앙선"));
        lines.put("신림선", Arrays.asList("수도권경량도시철도신림선", "신림"));
        lines.put("경춘선", Arrays.asList("경춘"));
        lines.put("경강선", Arrays.asList("경강"));
        lines.put("GTX-A", Arrays.asList("수도권광역급행철도"));
        lines.put("인천1호선", Arrays.asList("인천지하철1호선"));
        lines.put("인천2호선", Arrays.asList("인천지하철2호선"));
        lines.put("우이신설선", Arrays.asList("우이신설", "우이신설경전철"));
        lines.put("의정부경전철", Arrays.asList("의정부선","의정부"));
        lines.put("서해", Arrays.asList("서해선"));

    }

    private void setStationNames(){

        stationNames.put("자양", Arrays.asList("뚝섬유원지"));
        stationNames.put("서울", Arrays.asList("서울역"));
        stationNames.put("응암순환", Arrays.asList("응암"));
        stationNames.put("4.19민주묘지", Arrays.asList("4·19민주묘지"));
        stationNames.put("평택지제", Arrays.asList("지제"));
        stationNames.put("총신대입구", Arrays.asList("이수"));
        stationNames.put("불암산", Arrays.asList("당고개"));

    }

    public String cleanLine(String line) {
        if (line == null)
            return null;
        String cleanedLine = line
                .replaceAll("\\(.*?\\)", "") // 괄호와 괄호 안의 내용 전체 제거
                .replaceAll(" ", "") // 공백 제거
                .trim(); // 앞뒤 공백 제거

        for(Map.Entry<String, List<String>> entry : lines.entrySet()){
            if (entry.getValue().contains(cleanedLine)) {
                return entry.getKey();
            }
        }

        return cleanedLine;
    }

    public String cleanStationName(String stationName) {

        if (stationName == null)
            return null;
        stationName =  stationName
                .replaceAll("\\(.*?\\)", "") // 괄호와 괄호 안의 내용 전체 제거
                .replaceAll(" ", "") // 공백 제거
                .replaceAll("역$", "") // 마지막에 '역'이 있으면 제거
                .trim(); // 앞뒤 공백 제거

        stationName = stationName.replaceAll(" ", "");

        for(Map.Entry<String, List<String>> entry : stationNames.entrySet()){
            for(String name : entry.getValue()){
                if(name.equals(stationName)){
                    return entry.getKey();
                }
            }
        }

        return stationName;
    }

    public String createCode(String line, String name){

        return cleanLine(line) + "_" + cleanStationName(name);
    }

    public String createCode(_Station station){

        return cleanLine(station.getLineName()) + "_" + cleanStationName(station.getStationName());
    }

    public String cleanAddr(String addr) {

        return addr.replaceAll("\\(.*?\\)", ""); //괄호와 괄호 안의 내용 전체 제거
    }

    public int getTime(String time, String minute){

        int time_int, minute_int;
        if (time.isEmpty()) {
            time_int = 0;
        } else {
            time_int = Integer.parseInt(time);
        }

        if (minute.isEmpty()) {
            minute_int = 0;
        } else {
            minute_int = Integer.parseInt(minute);
        }

        return time_int * 60 + minute_int;
    }
}
