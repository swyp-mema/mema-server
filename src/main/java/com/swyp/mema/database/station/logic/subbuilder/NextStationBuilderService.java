package com.swyp.mema.database.station.logic.subbuilder;

import com.swyp.mema.database.station.model._NextStation;
import com.swyp.mema.database.station.model._Route;
import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.database.station.repository._NextStationRepository;
import com.swyp.mema.database.station.repository._RouteRepository;
import com.swyp.mema.database.station.repository._StationRepository;
import com.swyp.mema.database.station.util.ExcelReader;
import com.swyp.mema.database.station.util.StationConverter;
import com.swyp.mema.database.station.util.StringCleaner;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class NextStationBuilderService {

    /**
     *
     *      NextStation Entity를 생성하는 Class
     *      *** Station 데이터와 Route 데이터가 구축된 뒤 실행해야 함 ***
     *
     */

    private final _StationRepository stationRepository;
    private final _NextStationRepository nextStationRepository;
    private final ExcelReader excelReader;
    private final StringCleaner stringCleaner;
    private final StationConverter stationConverter;
    private final _RouteRepository routeRepository;

    private List<List<Integer>> countTimes;

    public NextStationBuilderService(_StationRepository stationRepository, _NextStationRepository nextStationRepository, ExcelReader excelReader, StringCleaner stringCleaner, StationConverter stationConverter, _RouteRepository routeRepository, TimeInfoService timeInfoService) {
        this.stationRepository = stationRepository;
        this.nextStationRepository = nextStationRepository;
        this.excelReader = excelReader;
        this.stringCleaner = stringCleaner;
        this.stationConverter = stationConverter;
        this.routeRepository = routeRepository;
        countTimes = timeInfoService.getCountTime();
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

    List<String> excludeLines = Arrays.asList("1호선", "2호선", "6호선");
    List<String> includeLines = Arrays.asList("1호선");


    /**
     *       Public Method
     *      `includeLines`에 포함된 지하철 호선을 대상으로 Build
     */
    @Transactional
    public void buildIncludeLineNextStation(){

        for (String line : includeLines) {

            buildLineNextStation(line);
        }
    }


    /**
     *      Public Method
     *      excludeLines에 존재하는 호선을 제외하고 Build
     */
    @Transactional
    public void buildExcludeLineNextStation(){

        HashSet<String> lines = new HashSet<>();
        ArrayList<ArrayList<String>> idTable = excelReader.readFile("/scheduleIds.xlsx");

        for(ArrayList<String> row : idTable){

            String line = row.get(0);
            if(excludeLines.contains(line)) continue;

            lines.add(line);
        }

        for (String line : lines) {

            buildLineNextStation(line);
        }
    }


    /**
     * 특정 호선에 속해있는 next station 데이터 구축
     * @param line 호선 명
     */
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

    /**
     * 엑셀 파일에서 특정 루트의 첫차 시간을 추출 (특정 호선의 특정 역에 대한 함수)
     * @param sheets    엑셀 데이터 시트
     * @param routeName 호선 명
     * @return  첫차 시간
     */
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


    /**
     * 특정 루트의 첫차시간을 정렬 기준으로 하는 Priority queue를 받아 해당 루트에서의 Next Station 데이터를 업데이트한다.
     * @param routeName 호선 명
     * @param entry     Priority queue
     */
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
}
