package com.swyp.mema.domain.midlocation.service;

import com.swyp.mema.database.station.model._NextStation;
import com.swyp.mema.database.station.model._Route;
import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.database.station.model._TransferStation;
import com.swyp.mema.database.station.repository.StationRepository;
import com.swyp.mema.database.station.util.StringCleaner;
import com.swyp.mema.domain.midlocation.dto.MidLocationDto;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.voteLocation.model.Location;
import lombok.Setter;
import lombok.val;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

@Service
public class MidLocationService {

    private final HashMap<String, _Station> codeMap;    // key: line + "_" + station name
    private final HashMap<String, _Station> idMap;      // key: Station.scheduleId
    private final StationRepository stationRepository;
    private final StringCleaner stringCleaner;

    public MidLocationService(StationRepository stationRepository, StringCleaner stringCleaner) {
        this.stationRepository = stationRepository;
        this.codeMap = new HashMap<>();
        this.idMap = new HashMap<>();
        this.stringCleaner = stringCleaner;
        init();
    }

    /* Inner Class : 각 유저들이 출발역에서 출발하였을 때 특정 역까지 가는 시간이 얼마나 걸리는지 기록 */
    class UserMap{

        HashMap<String, Integer> userMap;      // 도달하는데 걸리는 시간, key: _Station.scheduleId
        HashMap<String, String> prevMap;       // 이전 역의 scheduleId, key: _Station.scheduleId
        @Setter
        User user;
        @Setter
        _Station firstStation;

        public void initUsermap() {

            userMap = new HashMap<>();
            prevMap = new HashMap<>();
            for(_Station station : codeMap.values()){

                userMap.put(station.getScheduleId(), 10000);
                prevMap.put(station.getScheduleId(), station.getScheduleId());
            }
        }

        public void printAll(){
            Set<String> ids = idMap.keySet();
            for(String id : ids){
                String lineName = idMap.get(id).getLineName();
                String stationName = idMap.get(id).getStationName();
                System.out.println(lineName + "\t" + stationName + "\tuserMap:" + userMap.get(id) + "\tprevMap:" + prevMap.get(id));
            }
        }
    }

    @Transactional(readOnly = true)
    public void init() {

        List<_Station> totalStations = stationRepository.findAllForRuntimeAlgorithm();
        for (_Station station : totalStations) {

            String code = station.getLineName() + "_" + station.getStationName();
            codeMap.put(code, station);
            idMap.put(station.getScheduleId(), station);
        }
    }

    /**********************
     *   Public Method
     *********************/

    /**
     * 유저의 중간값 정보가 기록된 Location 클래스의 List를 받아 중간역과 그 경로를 반환
     * @param locations
     * @return
     */
    public Pair<_Station, List<MidLocationDto>> getTotalMidStation(List<Location> locations) {

        List<UserMap> userMaps = new ArrayList<>();
        for(Location location : locations) {

            UserMap userMap = new UserMap();
            userMap.initUsermap();
            userMap.setUser(location.getUser());
            userMap.setFirstStation(codeMap.get(stringCleaner.createCode(location.getStationRoute(),location.getStationName())));
            userMaps.add(userMap);

            //역 도달시간 계산
            calc(location.getStationName(), location.getStationRoute(), userMap, 1);

        }

        String midStationId = getMidStation(userMaps);
        Pair<_Station, List<MidLocationDto>> res = Pair.of(idMap.get(midStationId), new ArrayList<>());
        for(UserMap userMap : userMaps) {
            res.getSecond().add(MidLocationDto.builder()
                    .user(userMap.user)
                    .path(traceStations(userMap, midStationId))
                    .firstStation(userMap.firstStation)
                    .time(userMap.userMap.get(midStationId))
                    .build());
        }

        return res;
    }



    /**
     * UserMap과 출발 역 ID를 받아 도착역 -> 출발역 루트의 _Station Entity List를 반환
     * @param userMap
     * @param id    출발역의 scheduleId
     * @return      _Station Entity List (***역순)
     */
    private List<_Station> traceStations(UserMap userMap, String id) {

        String prevId = "";
        List<_Station> list = new ArrayList<>();
        while(!prevId.equals(id)){

            list.add(idMap.get(id));
            prevId = id;
            id = userMap.prevMap.get(id);
        }
        return list;
    }

    /**
     * UserMap List를 받아 해당 유저들의 중간역을 계산해서 반환
     * @param userMaps
     * @return  중간역의 scheduleId값
     */
    private String getMidStation(List<UserMap> userMaps){

        int size = userMaps.size();     //유저 수
        double minTime = Double.MAX_VALUE;
        String midId = "";

        for (String key : idMap.keySet()) {

            double distribute = 0;
            double sum = 0;
            double max_time = 0;


            for(int i=0; i<size; i++){

                Integer time = userMaps.get(i).userMap.get(key);
                if(time > max_time) max_time = time;
                sum += time;
            }
            for(int i=0; i<size; i++){
                Integer time = userMaps.get(i).userMap.get(key);
                distribute += pow((sum/size) - time, 2);
            }

            double deviation = sqrt(distribute);    //표준편차
            double stationScore = idMap.get(key).getTransferStations().size() * 10 * (size * 0.5 + 1);

            double score = sum + deviation + max_time - stationScore;         //점수 = 표준편차 + 이동시간 + 최대 이동시간 - 역의 번화도


            if (score < minTime) {
                minTime = score;
                midId = key;
            }
        }
        return midId;
    }
    /* 이동 시간 총 합 기반 중간역 계산 함수  */
    private String getMidStation1(List<UserMap> userMaps){

        int size = userMaps.size();
        int exclude = size/3;
        double minTime = Double.MAX_VALUE;
        String midId = "";

        for (String key : idMap.keySet()) {

            double val = 0;
            double sum = 0;
            /*
                중간역 계산시 가중치 알고리즘 : 중위값을 추종할 것인지, 평균값을 추종할 것인지
            */
            //- 중위값 추종
//            for(int i=exclude; i<size-exclude; i++){
//
//                sum += userMaps.get(i).userMap.get(key);
//            }
//            int aver = sum / (size - exclude * 2);

            List<Integer> times = new ArrayList<>();
            //- 평균값 추종
            for(int i=0; i<size; i++){

                sum += userMaps.get(i).userMap.get(key);
                times.add(userMaps.get(i).userMap.get(key));
            }
            double aver = sum / size;
            System.out.println(idMap.get(key).getLineName() + " "+idMap.get(key).getStationName()+": " + sum);

            times.sort(Integer::compareTo);

            val += sum;

//            val += (times.getLast() - times.getFirst())*0.8;
//            val += (times.get(1) - times.get(2))*0.4;

//            for(int i=0; i<exclude; i++){
//
//                val += (aver - times.get(i))*0.5;
//            }
//            for(int i=exclude; i<size-exclude; i++){
//
//                val += times.get(i);
//            }
//            for(int i=size-exclude; i<size; i++){
//
//                val += (times.get(i) - aver)*0.5;
//            }

            if (val < minTime) {
                minTime = val;
                midId = key;
            }
        }
        return midId;
    }

    /**
     * 유저가 출발역에서 출발하여 각각의 역까지 도달하는데 걸리는 시간을 계산하여 UserMap 클래스에 기록
     * @param stationName   출발역 명
     * @param line          출발역 호선
     * @param userMap       도달 시간 기록 Class
     * @param day           요일정보
     */
    private void calc(String stationName, String line, UserMap userMap, int day) {

        _Station curStation = codeMap.get(line + "_" + stationName);
        getSubway(curStation, curStation, userMap, 0, day);

        for (_TransferStation transferStation : curStation.getTransferStations()) {

            getSubway(curStation, curStation, userMap, transferStation.getTransferTime(), day);
        }
    }

    /**
     * 지하철 탑승 로직
     * 특정 역에서 지하철에 탑승하였을 때를 시뮬레이션 한다
     * @param prevStation
     * @param curStation
     * @param userMap
     * @param time
     * @param day
     */
    private void getSubway(_Station prevStation, _Station curStation, UserMap userMap, int time, int day){

        if (userMap.userMap.get(curStation.getScheduleId()) < time) return;
        Set<_Route> routeSet = curStation.getRoutes();
        toNextStation(prevStation, curStation, routeSet, userMap,time, 0, day);
    }

    /**
     * 특정 역에서 NextStation으로의 이동을 시뮬레이션
     * @param prevStation
     * @param curStation
     * @param routeSet
     * @param userMap
     * @param time
     * @param waitTime
     * @param day
     */
    private void toNextStation(_Station prevStation, _Station curStation, Set<_Route> routeSet, UserMap userMap, int time, int waitTime, int day) {

        //이미 매핑된 도달시간이 더 빠르면 return
        if (userMap.userMap.get(curStation.getScheduleId()) < time + waitTime) return;
        //도달시간, 이전역 정보 업데이트
        userMap.userMap.put(curStation.getScheduleId(), time + waitTime);
        userMap.prevMap.put(curStation.getScheduleId(), prevStation.getScheduleId());
//        System.out.println(curStation.getLineName() + "_" + curStation.getStationName() + ": " + time);

        //next station으로 이동
        for (_NextStation nextStation : curStation.getNextStations()) {

            //이전역은 패스
            if(Objects.equals(nextStation.getNextStation().getId(), prevStation.getId())) continue;

            //해당 역까지 갈 수 있는 루트를 체크해본다.
            Set<_Route> nextRouteSet = CheckRouteSet(routeSet, nextStation.getRoutes());
            if(nextRouteSet.isEmpty()) {
                //갈 수 있는 경로가 없으면 여기서 다시 탑승하는데?? 뭐지
                getSubway(prevStation, curStation, userMap, time + waitTime, day);
                continue;
            }

            int num = 1;
            int times = 0;
            int nextWaitTime;
            int nextTime = time + nextStation.getMoveTime();
            for (_Route route : nextRouteSet) {
                if(route.getTime(day)!=0)
                    times = route.getTime(day);
                num += route.getNum(day);
            }
            nextWaitTime = times / num;
            nextWaitTime /= 2;
            if(nextWaitTime == 0) nextWaitTime = 60;
            toNextStation(curStation, nextStation.getNextStation(), nextRouteSet, userMap, nextTime, nextWaitTime, day);
        }

        for (_TransferStation transferStation : curStation.getTransferStations()) {

            toTransferStation(curStation, transferStation.getTransferStation(), userMap, time + waitTime + transferStation.getTransferTime(), day);
        }
    }

    /**
     * 지하철 환승 시뮬레이션
     * @param prevStation
     * @param curStation
     * @param userMap
     * @param time
     * @param day
     */
    private void toTransferStation(_Station prevStation, _Station curStation, UserMap userMap, int time, int day) {

        getSubway(prevStation, curStation, userMap, time, day);
    }

    /**
     * routeSet의 Route들 중 routes에 포함되어 있는 것들을 Set으로 만들어 return
     * @param routeSet
     * @param routes
     * @return
     */
    private Set<_Route> CheckRouteSet(Set<_Route> routeSet, Set<_Route> routes) {

        Set<_Route> newRouteSet = new HashSet<>();
        for (_Route route : routeSet) {

            if (routes.contains(route)) {
                newRouteSet.add(route);
            }
        }

        return newRouteSet;
    }

}
