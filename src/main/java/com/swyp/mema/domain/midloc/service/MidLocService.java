package com.swyp.mema.domain.midloc.service;

import com.swyp.mema.domain.meet.exception.MeetNotFoundException;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.repository.MeetRepository;
import com.swyp.mema.domain.meetMember.exception.MeetMemberNotFoundException;
import com.swyp.mema.domain.meetMember.exception.NotMeetMemberException;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.midloc.service.structures.StationInfo;
import com.swyp.mema.domain.station.dto.response.subwayInfo.SingleStationResponse;
import com.swyp.mema.domain.user.exception.UserNotFoundException;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.repository.UserRepository;
import com.swyp.mema.domain.voteLocation.dto.response.MidLocationResponse;
import com.swyp.mema.domain.voteLocation.exception.LocationNotFoundException;
import com.swyp.mema.domain.voteLocation.model.Location;
import com.swyp.mema.domain.voteLocation.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MidLocService {

	private final OpenAIWorker openAIWorker;
	private final Stations stationService;
	private final LocationRepository locationRepository;
	private final MeetMemberRepository meetMemberRepository;
	private final MeetRepository meetRepository;
	private final UserRepository userRepository;

	/**
	 *
	 * meetId로 미팅의 중간역, 미팅 참가자들의 투표역 정보를 반환하는 메서드
	 * @param meetId
	 * @param userId
	 * @return
	 */
	@Transactional
	public MidLocationResponse getMidLocation(Long meetId, Long userId) {

		// 필수 검증 로직
		User user = validateUser(userId);
		Meet meet = validateMeet(meetId);
		MeetMember meetMember = validateMeetMember(user, meet);

		// 약속 ID와 약속원이 일치하는지 확인
		if (!meetMember.getMeet().getId().equals(meetId)) {
			throw new NotMeetMemberException();
		}

		// 유저들의 출발위치 responses
		List<Location> locations = locationRepository.findByMeetId(meetId);

		// 유저들의 출발 위치가 없는 경우 예외
		if (locations.isEmpty()) { throw new LocationNotFoundException(); }

		List<SingleStationResponse> userStartStations = locations.stream()
			.map(location -> SingleStationResponse.builder()
				.stationName(location.getStationName())
				.routeName(location.getStationRoute())
				.lat(location.getLat())
				.lot(location.getLot())
				.build())
			.toList();

		SingleStationResponse midStation = getMidStation(userId, meetId);

		return MidLocationResponse.builder()
			.startStationList(userStartStations)
			.midStation(midStation)
			.build();

	}

	/**
	 * meetId 미팅의 중간역을 반환하는 메서드
	 * @param userId
	 * @param meetId
	 * @return 이름, 혹은 라인이 매칭되지 않으면 null 반환
	 */
	public SingleStationResponse getMidStation(Long userId, Long meetId) {

		int maxRetry = 100;
		List<String> locs = locationRepository.findByMeetId(meetId).stream()
			.map(Location::getStationName)
			.toList();

		if (locs.isEmpty()) {
			throw new IllegalArgumentException("출발 위치 데이터가 없습니다.");
		}

		int peopleNum = locs.size();
		String basePrompt = "우리는 지금 " + peopleNum + "명이 모이려고 해. 우리의 출발 위치는 모두 지하철 역이야. 우리는 각각 ";
		basePrompt += makeString(locs);
		basePrompt += "에서 출발하려고 해. 우리 모두에게 공평한 지하철 역을 하나 찾아줘.";
		basePrompt += " 나는 다음과 같은 형태의 답변을 원해.다른 텍스트를 붙이지 말고 내가 원하는 형태의 답변만 적어줘.";
		basePrompt += "\n추쳔역: {추천하는 역 명}, 역의 호선: {추천하는 역의 호선}\n역의 호선은 하나만 적어줘.";

		for (int attempt = 1; attempt <= maxRetry; attempt++) {
			try {
				String prompt = basePrompt;

				// 추가적인 시도 시 Prompt 조정
				if (attempt > 1) {
					prompt += " (다른 결과를 제시해줘, 이전 결과는 만족스럽지 않았어.)";
				}

				String returnString = openAIWorker.callOpenAI(prompt);
				System.out.println(prompt);
				System.out.println(returnString);

				// OpenAI 응답 파싱
				String stationName = extractValue(returnString, "추쳔역: (.*?),");
				String stationLine = extractValue(returnString, "역의 호선: (\\d+호선)");

				stationName = cleanStationName(stationName);
				stationLine = cleanLine(stationLine);

				String stationCode = stationLine + "_" + stationName;

				StationInfo stationInfo = stationService.getStationInfos().get(stationCode);

				if (stationInfo != null) {
					return SingleStationResponse.builder()
						.stationName(stationName)
						.routeName(stationLine)
						.lot(stationInfo.getLot())
						.lat(stationInfo.getLat())
						.build();
				}

				System.out.println("Attempt " + attempt + ": Station not found. Retrying...");
			} catch (Exception e) {
				// 예외 처리 및 로깅
				System.err.println("Attempt " + attempt + ": Error during OpenAI call - " + e.getMessage());
			}
		}

		// Fallback 값 반환
		System.out.println("모든 시도가 실패했습니다. 기본값을 반환합니다.");

		return SingleStationResponse.builder()
			.stationName("기본역")
			.routeName("1호선")
			.lot("0.0")
			.lat("0.0")
			.build();
	}

	/**
	 * 문자열 처리
	 * @param  locs [XX역, YY역]      ##실제로는 '역' 글자 안들어가있음##
	 * @return  "XX역, YY역"
	 */
	public String makeString(List<String> locs) {

		String ret = "";
		for (String loc : locs) {

			ret = ret + loc + ",";
		}
		ret = ret.substring(0, ret.length() - 1);
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
		if (stationName == null)
			return null;
		return stationName
			.replaceAll("\\(.*?\\)", "") // 괄호와 괄호 안의 내용 전체 제거
			//                .replaceAll("역$", "") // 마지막에 '역'이 있으면 제거
			.trim(); // 앞뒤 공백 제거
	}

	private String cleanLine(String line) {
		if (line == null)
			return null;
		String cleanedLine = line
			.replaceAll("\\(.*?\\)", "") // 괄호와 괄호 안의 내용 전체 제거
			.trim(); // 앞뒤 공백 제거

		if ("수인선".equals(cleanedLine) || "분당선".equals(cleanedLine)) {
			return "수인분당선";
		}
		return cleanedLine;
	}
	private MeetMember validateMeetMember(User user, Meet meet) {
		return meetMemberRepository.findByUserAndMeet(user, meet)
			.orElseThrow(NotMeetMemberException::new);
	}

	private MeetMember validateMeetMember(Long meetMemberId) {
		return meetMemberRepository.findById(meetMemberId)
			.orElseThrow(MeetMemberNotFoundException::new);
	}

	private Meet validateMeet(Long meetId) {
		return meetRepository.findById(meetId)
			.orElseThrow(MeetNotFoundException::new);
	}

	private User validateUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
	}
}
