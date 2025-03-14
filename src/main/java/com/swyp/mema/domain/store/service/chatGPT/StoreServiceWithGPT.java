package com.swyp.mema.domain.store.service.chatGPT;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.swyp.mema.domain.meet.exception.MeetNotFoundException;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.repository.MeetRepository;
import com.swyp.mema.domain.meetMember.exception.NotMeetMemberException;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.meetMember.repository.MeetMemberRepository;
import com.swyp.mema.domain.store.dto.naverAPI.StoreRes;
import com.swyp.mema.domain.store.dto.naverAPI.TotalStoreRes;
import com.swyp.mema.domain.store.exception.NotRecommendStore;
import com.swyp.mema.domain.user.exception.UserNotFoundException;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.repository.UserRepository;
import com.swyp.mema.domain.voteLocation.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreServiceWithGPT {

	private final OpenAIWorker openAIWorker;
	private final UserRepository userRepository;
	private final MeetRepository meetRepository;
	private final MeetMemberRepository meetMemberRepository;
	private final LocationRepository locationRepository;

	public TotalStoreRes recommendStore(Long userId, Long meetId) {

		// 필수 검증 로직
		User user = validateUser(userId);
		Meet meet = validateMeet(meetId);
		validateMeetMember(user, meet);

		int maxRetry = 10;
		int voteLocCount = locationRepository.findByMeetId(meetId).size();

		if (voteLocCount == 0 || voteLocCount == 1) {
			throw new NotRecommendStore();
		}

		String meetLocation = meet.getMeetLocation();
		String lat = meet.getLat();
		String lot = meet.getLot();

		String basePrompt = meetLocation + "(위도 : " + lat + ", 경도 : " + lot + ") 근처의 음식점을 4개 추천해줘.";
		basePrompt += " 나는 다음과 같은 형태의 답변을 원해.다른 텍스트를 붙이지 말고 내가 원하는 형태의 답변만 적어줘.";
		basePrompt += "\n가게 이름: {가게 이름}\n간략한 가게 정보: {가게 정보}\n가게 카테고리: {가게 카테고리}\n가게 주소: {가게 주소}\n영업 시간: {영업 시간}\n가게 전화번호: {가게 전화번호}\n평점: {평점}";

		for (int attempt = 1; attempt <= maxRetry; attempt++) {
			try {
				String prompt = basePrompt;

				// 추가적인 시도 시 Prompt 조정
				if (attempt > 1) {
					prompt += " (다른 결과를 제시해줘, 이전 결과는 만족스럽지 않았어.)";
				}

				String returnString = openAIWorker.callOpenAI(prompt);
				System.out.println("prompt = " + prompt);
				System.out.println("===============================");
				System.out.println(returnString);

				// OpenAI 응답 파싱
				TotalStoreRes storeRes = parseStores(returnString);

				// 응답이 유효한 경우 반복 종료
				if (storeRes != null && !storeRes.getStores().isEmpty()) {
					return storeRes;
				}

				System.out.println("Attempt " + attempt + ": No valid stores found. Retrying...");

			} catch (Exception e) {
				// 예외 처리 및 로깅
				System.err.println("Attempt " + attempt + ": Error during OpenAI call - " + e.getMessage());
			}
		}
		// 최대 재시도 초과 시 예외를 던져 무조건 값을 받을 수 있도록 강제
		throw new RuntimeException("OpenAI API 호출 실패: 모든 재시도가 실패했습니다.");
	}

	public static TotalStoreRes parseStores(String data) {

		// 입력 데이터 끝에 개행 문자 보장
		if (!data.endsWith("\n")) {
			data += "\n";
		}

		// 정규식 패턴 정의
		String regex = "가게 이름: (.*?)\\n" +
			"간략한 가게 정보: (.*?)\\n" +
			"가게 카테고리: (.*?)\\n" +
			"가게 주소: (.*?)\\n" +
			"영업 시간: (.*?)\\n" +
			"가게 전화번호: (.*?)\\n" +
			"평점: (.*?)(?:\\n|$)";

		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(data);

		// 결과를 저장할 리스트
		List<StoreRes> stores = new ArrayList<>();

		while (matcher.find()) {
			// Shop 객체 생성
			StoreRes store = StoreRes.builder()
				.name(matcher.group(1))
				// .description(matcher.group(2))
				.category(matcher.group(3))
				.address(matcher.group(4))
				// .time(matcher.group(5))
				// .phone(matcher.group(6))
				// .score(matcher.group(7))
				.build();

			// 리스트에 추가
			stores.add(store);
		}

		return new TotalStoreRes(stores);
	}

	private MeetMember validateMeetMember(User user, Meet meet) {
		return meetMemberRepository.findByUserAndMeet(user, meet)
			.orElseThrow(NotMeetMemberException::new);
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
