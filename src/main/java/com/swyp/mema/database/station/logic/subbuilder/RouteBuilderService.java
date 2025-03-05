package com.swyp.mema.database.station.logic.subbuilder;

import com.swyp.mema.database.station.model._Route;
import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.database.station.repository._RouteRepository;
import com.swyp.mema.database.station.repository.StationRepository;
import com.swyp.mema.database.station.util.ExcelReader;
import com.swyp.mema.database.station.util.StringCleaner;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteBuilderService {

    private final _RouteRepository routeRepository;
    private final StationRepository stationRepository;
    private final ExcelReader excelReader;
    private final StringCleaner stringCleaner;

    private List<List<Integer>> countTimes;

    public RouteBuilderService(_RouteRepository routeRepository, StationRepository stationRepository, ExcelReader excelReader, StringCleaner stringCleaner, TimeInfoService timeInfoService) {
        this.routeRepository = routeRepository;
        this.stationRepository = stationRepository;
        this.excelReader = excelReader;
        this.stringCleaner = stringCleaner;
        countTimes = timeInfoService.getCountTime();
    }

    List<String> excludeLines = Arrays.asList("2호선", "6호선");
    List<String> includeLines = Arrays.asList("4호선");

    /**
     *      Public Method
     *      excludeLines에 존재하는 호선을 제외하고 Build
     */
    public void buildExcludeRoute(){

        HashSet<_Route> routeHashSet = new HashSet<>(routeRepository.findAll());
        HashSet<String> lines = new HashSet<>();
        ArrayList<ArrayList<String>> idTable = excelReader.readFile("/scheduleIds.xlsx");
        for(ArrayList<String> row : idTable){

            String line = row.get(0);
            if(excludeLines.contains(line)) continue;

            lines.add(line);
        }

        for (String line : lines) {
            List<_Route> lineRoutes = getLineRoutes(line);
            routeHashSet.addAll(lineRoutes);
        }

        routeRepository.saveAll(routeHashSet);
    }

    /**
     *      Public Method
     *      includeLines에 존재하는 호선만 Build
     */
    public void buildIncludeRoute(){

        HashSet<_Route> routeHashSet = new HashSet<>(routeRepository.findAll());
        List<String> lines = includeLines;

        for (String line : lines) {
            List<_Route> lineRoutes = getLineRoutes(line);
            routeHashSet.addAll(lineRoutes);
        }

        routeRepository.saveAll(routeHashSet);
    }


    /**
        특정 라인에 대해 Route 생성
     */
    private List<_Route> getLineRoutes(String line){

        List<_Station> lineStations = stationRepository.findByLineName(line);
        HashSet<_Route> routes = new HashSet<>();
        for(_Station station : lineStations){

            List<_Route> routeList = getRoutesAtStation(station);
            routes.addAll(routeList);
        }
        return routes.stream().toList();
    }

    /**
        특정 엑셀파일(단일 역)에 대해 Route 생성
     */
    private List<_Route> getRoutesAtStation(_Station station){

        String stationName = station.getStationName();
        String line = station.getLineName();

        HashMap<String, _Route> routeMap = new HashMap<>();   //key: 루트 이름
        String path = "/schedules/" + line + "/" + stationName + ".xlsx";   //엑셀 파일 패스
        for(int i=1; i<=3; i++){

            HashMap<String, Integer> routeCountMap = new HashMap<>();
            ArrayList<ArrayList<String>> data = excelReader.readFile(path, String.valueOf(i));
            for(ArrayList<String> row : data){

                if (row.get(3).equals("급행")) continue;

                int time = stringCleaner.getTime(row.get(0), row.get(1));
                if(time < countTimes.get(i-1).get(0) || time > countTimes.get(i-1).get(1)) continue;

                String route = row.get(5) + "-" + row.get(6);
                if(!routeCountMap.containsKey(route)) routeCountMap.put(route, 0);
                routeCountMap.put(route, routeCountMap.get(route) + 1);
            }

            for(Map.Entry<String, Integer> entry : routeCountMap.entrySet()){

                String route = entry.getKey();
                if(routeRepository.existsByRoute(route)) continue;
                if(!routeMap.containsKey(route)) routeMap.put(route, _Route.builder()
                        .route(route)
                        .line(line)
                        .build().initNums());

                routeMap.get(entry.getKey()).addNum(i, entry.getValue(), countTimes.get(i-1).get(1) - countTimes.get(i-1).get(0));
            }
        }
        station.addRoutes(routeMap.values().stream().toList());

        return routeMap.values().stream().toList();
    }

}
