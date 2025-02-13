package com.swyp.mema.database.station.logic.subbuilder;

import com.swyp.mema.database.station.model._NextStation;
import com.swyp.mema.database.station.model._Route;
import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.database.station.repository._NextStationRepository;
import com.swyp.mema.database.station.repository._RouteRepository;
import com.swyp.mema.database.station.repository._StationRepository;
import com.swyp.mema.database.station.repository._TransferStationRepository;
import com.swyp.mema.database.station.util.ExcelReader;
import com.swyp.mema.database.station.util.StationConverter;
import com.swyp.mema.database.station.util.StringCleaner;
import com.swyp.mema.domain.station.model.Station;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class NextStationBuilder {

    private final _StationRepository stationRepository;
    private final _NextStationRepository nextStationRepository;
    private final ExcelReader excelReader;
    private final StringCleaner stringCleaner;
    private final StationConverter stationConverter;
    private final _RouteRepository routeRepository;

    private List<List<Integer>> countTimes;

    public NextStationBuilder(_StationRepository stationRepository, _NextStationRepository nextStationRepository, ExcelReader excelReader, StringCleaner stringCleaner, StationConverter stationConverter, _RouteRepository routeRepository, Info info) {
        this.stationRepository = stationRepository;
        this.nextStationRepository = nextStationRepository;
        this.excelReader = excelReader;
        this.stringCleaner = stringCleaner;
        this.stationConverter = stationConverter;
        this.routeRepository = routeRepository;
        countTimes = info.getCountTime();
    }

    /*  Inner class: 특정 루트에 대한 각 역들의 정보 */
    @AllArgsConstructor
    class RouteStationInfo implements Comparable<RouteStationInfo> {

        String stationCode; //역 코드
        Integer time;       //루트의 첫차시간

        Integer num1;       //평일 배차 횟수
        Integer num2;       //토요일 배차 횟수
        Integer num3;       //일요일 & 공휴일 배차 횟수

        @Override
        public int compareTo(RouteStationInfo o) {
            return this.time - o.time;
        }
    }

    @AllArgsConstructor
    class StationInfo implements Comparable<StationInfo> {

        String line;
        String stationName;
        int firstTime;

        @Override
        public int compareTo(StationInfo o) {
            return this.firstTime - o.firstTime;
        }
    }

    List<String> excludeLines = Arrays.asList("2호선", "6호선");
    List<String> includeLines = Arrays.asList("4호선");


    /*******************************************************************
     *
     * station DB에 저장된 역들에 next station 정보 추가
     *
     *******************************************************************/

    @Transactional
    public void buildIncludeLineNextStation(){

        for (String line : includeLines) {

            buildLineNextStation(line);
        }
    }



    private void buildLineNextStation(String line){

        HashMap<String, PriorityQueue<StationInfo>> linePqMap = buildLinePqMap(line);

        for (Map.Entry<String, PriorityQueue<StationInfo>> entry : linePqMap.entrySet()) {

            String routeName = entry.getKey();
            updateRouteNextStations(routeName, entry.getValue());
        }
        for(String routeName : linePqMap.keySet())
            updateNextStationOfRouteLastStation(line, routeName);
    }


    /**
     * 특정 호선에 속하는 route들의 첫차시간 pq 생성 함수
     * @param line  호선 명
     * @return  key: route 명,   values: 해당 route에 속한 지하철 역들의 첫차 시간 (sorted)
     */
    private HashMap<String, PriorityQueue<StationInfo>> buildLinePqMap(String line){

        List<_Station> lineStations = stationRepository.findByLineName(line);
        List<_Route> lineRoutes = routeRepository.findByLine(line);
        HashMap<String, PriorityQueue<StationInfo>> pqMap = new HashMap<>();

        // Route 이름을 key로 갖는 PQ 생성
        for(_Route routeName : lineRoutes){
            pqMap.put(routeName.getRoute(), new PriorityQueue<>());
        }

        for (_Station station : lineStations) {
            String stationName = station.getStationName();
            String path = "/schedules/" + line + "/" + stationName + ".xlsx";
            List<ArrayList<ArrayList<String>>> sheets = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                sheets.add(excelReader.readFile(path, String.valueOf(i)));
            }
            Set<_Route> routes = station.getRoutes();
            for (_Route route : routes) {
                String routeName = route.getRoute();
                int firstTime = findFirstSchedule(sheets, routeName);

                pqMap.get(routeName).offer(new StationInfo(line, stationName, firstTime));
            }
        }

        return pqMap;
    }

    private int findFirstSchedule(List<ArrayList<ArrayList<String>>> sheets, String routeName){

        for(ArrayList<ArrayList<String>> data : sheets) {
            for (ArrayList<String> row : data) {
                if (row.get(3).equals("급행")) continue;
                String route = row.get(5) + "-" + row.get(6);

                if (!route.equals(routeName)) continue;

                return stringCleaner.getTime(row.get(0), row.get(1));
            }
        }

        return 0;
    }


    private void updateRouteNextStations(String routeName, PriorityQueue<StationInfo> entry) {

        _Route route = routeRepository.findById(routeName).isPresent() ? routeRepository.findById(routeName).get() : null;
        if(route == null) {
            System.out.println("route error");
            return;
        }

        StationInfo curInfo;
        StationInfo nextInfo = entry.poll();

        while (!entry.isEmpty()) {

            curInfo = nextInfo;
            nextInfo = entry.poll();
            assert curInfo != null;
            _Station curStation = stationRepository.findByLineNameAndStationName(route.getLine(), curInfo.stationName);
            _Station nextStation = stationRepository.findByLineNameAndStationName(route.getLine(), nextInfo.stationName);
            _NextStation nextStationEntity;

            int moveTime = nextInfo.firstTime - curInfo.firstTime;

            if (nextStationRepository.existsByCurStationAndNextStation(curStation, nextStation)) {
                // curStation -> nextStation 으로 가는  _NextStation 엔티티가 이미 존재한다면 가져오기

                nextStationEntity = nextStationRepository.findByCurStationAndNextStation(curStation, nextStation);
            } else {
                // 없다면 새로 생성

                nextStationEntity = _NextStation.builder()
                        .curStation(curStation)
                        .nextStation(nextStation)
                        .moveTime(moveTime)
                        .build();
                nextStationRepository.save(nextStationEntity);
                curStation.addNextStation(nextStationEntity);
            }
            for(int day=1; day<=3; day++){

                nextStationEntity.addNum(day, route.getNum(day));
            }
        }
    }

    /**
     * 각 루트의 종점역 직전역 -> 종점역 인 next station 정보 업데이트
     * @param line  호선 명
     * @param routeName 루트 명
     */
    private void updateNextStationOfRouteLastStation(String line, String routeName) {

        String lastStationName = routeName.split("-")[1];
        _Route targetRoute = routeRepository.findByRoute(routeName);
        _Station lastStation = stationRepository.findByLineNameAndStationName(line, lastStationName);

        _Station prevStation = null;
        _NextStation last2prev = null;
        boolean flag = false;

        assert lastStation.getNextStations() != null;
        for (_NextStation nextStationEntity : lastStation.getNextStations()) {

            prevStation = nextStationEntity.getNextStation();
            last2prev = nextStationEntity;
            for (_Route route : prevStation.getRoutes()) {

                //previous station의 route중에 우리가 찾는 route가 있으면 스탑
                if (route.getRoute().equals(routeName)) {
                    flag = true;
                    break;
                }
            }
            if(flag) break;
        }

        assert flag;

        _NextStation nextStation = nextStationRepository.findByCurStationAndNextStation(prevStation, lastStation);
        if (nextStation==null) {

            nextStation = _NextStation.builder()
                    .curStation(prevStation)
                    .nextStation(lastStation)
                    .moveTime(last2prev.getMoveTime())
                    .build();
            nextStationRepository.save(nextStation);
        }
        for(int day=1; day<=3; day++){

            nextStation.addNum(day, targetRoute.getNum(day));
        }
    }


    /**
     * 역 정보 추가 함수
     */
//    @Transactional
//    public void buildStationRelation() {
//
//        HashMap<String, _Station> stations = stationConverter.list2Hashmap(stationRepository.findAll());
//        stations = applyConditions(stations);
//        HashMap<String, HashSet<String>> stationRouteSet = getStationRouteSet(stations);
//        HashMap<String, ArrayList<RouteStationInfo>> routeStations = getRouteStations(stationRouteSet);
//
//        List<_Route> routes = createRoutes(routeStations);
//        routes = routeRepository.saveAll(routes);
//        List<_NextStation> newNextStations = addNextStations(stations, routeStations, routes);
//        nextStationRepository.saveAll(newNextStations);
//        List<_NextStation> lastNextStations = addNextStationsToLastStations(stations,routeStations);
//        nextStationRepository.saveAll(lastNextStations);
//    }

    private HashMap<String, _Station> applyConditions(HashMap<String, _Station> stations) {

        for(Iterator<Map.Entry<String, _Station>> itr = stations.entrySet().iterator(); itr.hasNext(); ) {
            _Station nextStation = itr.next().getValue();
            if (stationFilter(nextStation)) {
                itr.remove();
            }
        }

        return stations;
    }

    private boolean stationFilter(_Station station){

        if (station.getLineName().equals("2호선")) {

            return true;
        }
        return false;
    }


    /**
     * 인자로 받은 stations 속 _Station 클래스에 nextStation 추가
     * @param stations          key: station code,      value: _Station 클래스
     * @param routeStations     key: route name,        value: 각 루트의 역들 정보
     * @param routes                                    value: 루트 이름들
     * @return 새로 생성한 _NextStation 엔티티들
     */
    private ArrayList<_NextStation> addNextStations(HashMap<String, _Station> stations,
                                                    HashMap<String, ArrayList<RouteStationInfo>> routeStations,
                                                    List<_Route> routes) {

        ArrayList<_NextStation> newNextStations = new ArrayList<>();
        // 각 루트들에 대해서 실행
        for (_Route route : routes) {

            String routeName = route.getRoute();
            ArrayList<RouteStationInfo> routeStationInfos = routeStations.get(routeName);
            int size = routeStationInfos.size();

            // 해당 루트의 지하철 연결 (_NextStation 생성)
            for (int i=0; i<size; i++) {

                RouteStationInfo curInfo = routeStationInfos.get(i);
                if(i+1 >= size) break;

                RouteStationInfo nextInfo = routeStationInfos.get(i + 1);
                _Station curStation = stations.get(curInfo.stationCode);
                _Station nextStation = stations.get(nextInfo.stationCode);
                _NextStation nextStationEntity;
                int moveTime = nextInfo.time - curInfo.time;
                if(moveTime <= 0) {
                    System.out.println("sort order error!");
                    System.out.println(nextInfo.stationCode + "<-" + curInfo.stationCode + ": " + routeName);
                    System.out.println(nextInfo.time + " - " + curInfo.time);
                    return null;
                }
                if (nextStationRepository.existsByCurStationAndNextStation(curStation, nextStation)) {
                    // curStation -> nextStation 으로 가는  _NextStation 엔티티가 이미 존재한다면 가져오기

                    nextStationEntity = nextStationRepository.findByCurStationAndNextStation(curStation, nextStation);
                } else {
                    // 없다면 새로 생성

                    nextStationEntity = _NextStation.builder()
                            .curStation(curStation)
                            .nextStation(nextStation)
                            .moveTime(moveTime)
                            .build();
                    curStation.addNextStation(nextStationEntity);
                    newNextStations.add(nextStationEntity);
                }

                // _NextStation 엔티티에 route 추가하기
//                nextStationEntity.addRoute(route);
            }
        }

        return newNextStations;
    }

    private List<_NextStation> addNextStationsToLastStations(HashMap<String, _Station> stations, HashMap<String, ArrayList<RouteStationInfo>> routeStations) {

        ArrayList<_NextStation> newNextStations = new ArrayList<>();
        for(String route : routeStations.keySet()) {

            String lastStationName = route.split("-")[1];
            String line = routeStations.get(route).getFirst().stationCode.split("_")[0];
            _Station lastStation = stations.get(stringCleaner.createCode(line, lastStationName));
            if (lastStation == null) {

                System.out.println("\n" + route + ", first: " + routeStations.get(route).getFirst().stationCode);
                System.out.println(line + " " + lastStationName + " not found");
//                continue;
            }
            for (_NextStation toPrevStation : lastStation.getNextStations()) {

                assert toPrevStation.getCurStation().getStationName().equals(lastStationName);
                if (!nextStationRepository.existsByCurStationAndNextStation(toPrevStation.getNextStation(), toPrevStation.getCurStation())) {

                    _NextStation newNextStation;
                    int moveTime = toPrevStation.getMoveTime();
                    lastStation.addNextStation(
                            newNextStation = _NextStation.builder()
                                    .curStation(toPrevStation.getNextStation())
                                    .nextStation(toPrevStation.getCurStation())
                                    .moveTime(moveTime)
                                    .build());

                    newNextStations.add(newNextStation);
                }
            }
        }
        return newNextStations;
    }

//    /**
//     * _Route Entity 생성
//     * @param routeStations key: route 이름,  values: RouteStationInfo들의 List => 각 루트들에 포함된 역 정보
//     * @return
//     */
//    private List<_Route> createRoutes(HashMap<String, ArrayList<RouteStationInfo>> routeStations) {
//
//        List<_Route> routes = new ArrayList<>();
//        for (Map.Entry<String, ArrayList<RouteStationInfo>> routeStation : routeStations.entrySet()) {
//
//            String routeName = routeStation.getKey();
//            RouteStationInfo routeStationInfo = routeStation.getValue().getFirst();
//            int num1, num2, num3;
//            num1 = routeStationInfo.num1;
//            num2 = routeStationInfo.num2;
//            num3 = routeStationInfo.num3;
//
//            _Route route = new _Route(routeName,
//                    num1, countTimes.get(0).get(1) - countTimes.get(0).get(0),
//                    num2, countTimes.get(1).get(1) - countTimes.get(1).get(0),
//                    num3, countTimes.get(2).get(1) - countTimes.get(2).get(0)
//            );
//
//            routes.add(route);
//        }
//
//        return routes;
//    }


    /**
     *
     * @param stations key: station code, value: _Station Entity
     * @return key: station code, value: 해당 역에 존재하는 route들의 set
     */
    private HashMap<String, HashSet<String>> getStationRouteSet(HashMap<String, _Station> stations) {

        HashMap<String, HashSet<String>> stationRouteSet = new HashMap<>();

        for (_Station station : stations.values()) {

            HashSet<String> routeSet = new HashSet<>();
            String line = station.getLineName();
            String stationName = station.getStationName();
            String code = stringCleaner.createCode(line, stationName);

            String path = "/schedules/" + line + "/" + stationName + ".xlsx";
            for(int i=1; i<=3; i++) {
                ArrayList<ArrayList<String>> stationSchedule = excelReader.readFile(path, String.valueOf(i));

                for (ArrayList<String> row : stationSchedule) {

                    if(row.get(3).equals("급행")) continue;
                    String route = row.get(5) + "-" + row.get(6);
                    routeSet.add(route);
                }

            }
            stationRouteSet.put(code, routeSet);
        }

        return stationRouteSet;
    }

    /**
     *  루트당 역 정보 생성
     * @param stationRouteSet key: station code, value: 해당 역에 존재하는 route들의 set
     * @return key: route name, value: 해당 route내 존재하는 역들의 운행정보
     */
    private HashMap<String, ArrayList<RouteStationInfo>> getRouteStations(HashMap<String,
            HashSet<String>> stationRouteSet) {
        HashMap<String, ArrayList<RouteStationInfo>> routeStations = new HashMap<>();

        // 각 station에 대하여 실행
        for(Map.Entry<String, HashSet<String>> entry : stationRouteSet.entrySet()) {

            String code = entry.getKey();
            String filepath = "/schedules/" + code.replace("_", "/") + ".xlsx";
            ArrayList<ArrayList<String>> data1 = excelReader.readFile(filepath, "1");
            ArrayList<ArrayList<String>> data2 = excelReader.readFile(filepath, "2");
            ArrayList<ArrayList<String>> data3 = excelReader.readFile(filepath, "3");

            List<ArrayList<ArrayList<String>>> data = new ArrayList<>(Arrays.asList(data1, data2, data3));

            HashMap<String, List<Integer>> countPerRoute = new HashMap<>();

            // route당 배차수 카운트하는 hashmap 준비
            for (String route : entry.getValue()) {

                countPerRoute.put(route, new ArrayList<>(Arrays.asList(new Integer []{0, 0, 0})));
            }

            // route당 배차수 카운트
            int i=0;
            for (ArrayList<ArrayList<String>> datum : data) {

                for (ArrayList<String> row : datum) {

                    if(row.get(3).equals("급행")) continue;
                    int time = stringCleaner.getTime(row.get(0), row.get(1));
//                    if(time < countTimes[i][0] || time > countTimes[i][1]) continue;

                    String route = row.get(5) + "-" + row.get(6);
                    countPerRoute.get(route).set(i, countPerRoute.get(route).get(i) + 1);
                }
                i++;
            }

            // 각 루트의 첫차 시간 계산
            HashMap<String, Integer> routeTime = new HashMap<>();
            for(ArrayList<ArrayList<String>> datum : data) {
                for (ArrayList<String> row : datum) {

                    if (row.get(3).equals("급행")) continue;
                    String route = row.get(5) + "-" + row.get(6);

                    if (routeTime.containsKey(route)) continue;

                    int time = stringCleaner.getTime(row.get(0), row.get(1));
                    routeTime.put(route, time);
                }
            }

            HashSet<String> routeSet = entry.getValue();

            // (해당 역의) 각 루트별 RouteStationInfo 생성
            for(String route : routeSet){

                int time = routeTime.get(route);
                RouteStationInfo info = new RouteStationInfo(
                        code, time,
                        countPerRoute.get(route).get(0),
                        countPerRoute.get(route).get(1),
                        countPerRoute.get(route).get(2)
                );

                if (!routeStations.containsKey(route)) {

                    routeStations.put(route, new ArrayList<>());
                }
                routeStations.get(route).add(info);
            }

        }

        // 생성된 routeStations를 첫 차 시간 순으로 정렬
        for(ArrayList<RouteStationInfo> infoList: routeStations.values()) {

            Collections.sort(infoList);
        }

        return routeStations;
    }

}
