package com.swyp.mema.domain.midloc.service;


import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.midloc.service.structures.StationInfo;
import com.swyp.mema.domain.station.dto.response.subwayInfo.SingleStationResponse;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.voteLocation.dto.response.MidLocationResponse;
import com.swyp.mema.domain.voteLocation.dto.response.TotalLocationResponse;
import com.swyp.mema.domain.voteLocation.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MidLocService {

    private final OpenAIWorker openAIWorker;
    private final LocationService locationService;
    private final Stations stationService;

    /**
     *
     * meetId로 미팅의 중간역, 미팅 참가자들의 투표역 정보를 반환하는 메서드
     * @param meetId
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public MidLocationResponse getMidLocation(Long meetId, Long userId) {

        // 필수 검증 로직
        User user = locationService.validateUser(userId);
        Meet meet = locationService.validateMeet(meetId);
        MeetMember meetMember = locationService.validateMeetMember(user, meet);
        locationService.validateMeetMember(meetMember.getId());


        MidLocationResponse response = MidLocationResponse.builder()
                .midStation(getMidStation(userId, meetId))
                .build();

//        List<Location> locations = locationRepository.findByMeetId(meetId);

        /**
         ###                                                                                    ###
		 ### 여기서 response 안에  참가자들의 투표 데이터를 SingleStationResponse 형식으로 넣어주세요.   ###
         ###                                                                                    ###
         */

        return response;
    }


    /**
     * meetId 미팅의 중간역을 반환하는 메서드
     *
     * @param userId
     * @param meetId
     * @return 이름, 혹은 라인이 매칭되지 않으면 null 반환
     */
    public SingleStationResponse getMidStation(Long userId, Long meetId){

        TotalLocationResponse reses = locationService.getTotalLocation(meetId, userId);
        List<String> locs = reses.getStartStationList();


        int peopleNum = locs.size();
        String prompt = "우리는 지금 " + String.valueOf(peopleNum) + "명이 모이려고 해. 우리의 출발 위치는 모두 지하철 역이야. 우리는 각각 ";
        prompt += makeString(locs);
        prompt += "에서 출발하려고 해.";
        prompt += " 우리 모두에게 공평한 지하철 역을 하나 찾아줘. 나는 다음과 같은 형태의 답변을 원해.다른 텍스트를 붙이지 말고 내가 원하는 형태의 답변만 적어줘.";
        prompt += "\n추쳔역: {추천하는 역 명}, 역의 호선: {추천하는 역의 호선}\n역의 호선은 하나만 적어줘.";

        String returnString = openAIWorker.callOpenAI(prompt);
        System.out.println(prompt);
        System.out.println(returnString);

        String stationName = extractValue(returnString, "추쳔역: (.*?),");
        String stationLine = extractValue(returnString, "역의 호선: (\\d+호선)");

        //  역 이름, 호선 파싱     **괄호 지우기, 수인선, 분당선 -> 수인분당선 등등**
        stationName = cleanStationName(stationName);
        stationLine = cleanLine(stationLine);


        String stationCode = stationLine + "_" + stationName;
        StationInfo stationInfo = stationService.getStationInfos().get(stationCode);
        if(stationInfo == null){

            return null;
        }

        return SingleStationResponse.builder()
                .stationName(stationName)
                .routeName(stationLine)
                .lot(stationInfo.getLot())
                .lat(stationInfo.getLat())
                .build();
    }

    /**
     * 문자열 처리
     * @param  locs [XX역, YY역]      ##실제로는 '역' 글자 안들어가있음##
     * @return  "XX역, YY역"
     */
    public String makeString(List<String> locs){

        String ret = "";
        for(String loc : locs){

            ret = ret + loc + ",";
        }
        ret = ret.substring(0, ret.length()-1);
        return ret;
    }

    /**
     * 역 이름, 호선 파싱 메서드
     *
     * @param input  입력 문자열
     * @param regex  추출할 정규식 패턴
     * @return 추출된 값, 없으면 빈 문자열 반환
     */
    public static String extractValue(String input, String regex) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    /**
     * 역 이름의 괄호 등 제거 메서드
     */
    private String cleanStationName(String stationName) {
        if (stationName == null) return null;
        return stationName
                .replaceAll("\\(.*?\\)", "") // 괄호와 괄호 안의 내용 전체 제거
//                .replaceAll("역$", "") // 마지막에 '역'이 있으면 제거
                .trim(); // 앞뒤 공백 제거
    }

    private String cleanLine(String line) {
        if (line == null) return null;
        String cleanedLine = line
                .replaceAll("\\(.*?\\)", "") // 괄호와 괄호 안의 내용 전체 제거
                .trim(); // 앞뒤 공백 제거

        if ("수인선".equals(cleanedLine) || "분당선".equals(cleanedLine)) {
            return "수인분당선";
        }
        return cleanedLine;
    }

}
