package com.swyp.mema.database.station.util;

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
        lines.put("4호선", Arrays.asList("과천선"));
    }

    private void setStationNames(){
        stationNames.put("자양", Arrays.asList("뚝섬유원지"));
    }

    public String cleanLine(String line) {
        if (line == null)
            return null;
        String cleanedLine = line
                .replaceAll("\\(.*?\\)", "") // 괄호와 괄호 안의 내용 전체 제거
                .trim(); // 앞뒤 공백 제거

        for(Map.Entry<String, List<String>> entry : lines.entrySet()){
            for(String name : entry.getValue()){
                if(name.equals(cleanedLine)){
                    return entry.getKey();
                }
            }
        }

        return cleanedLine;
    }

    public String cleanStationName(String stationName) {

        if (stationName == null)
            return null;
        stationName =  stationName
                .replaceAll("\\(.*?\\)", "") // 괄호와 괄호 안의 내용 전체 제거
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

//        return cleanLine(line) + "_" + cleanStationName(name);
        return line + "_" + name;
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
