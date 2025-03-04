package com.swyp.mema.domain.store.service.naverAPI;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.swyp.mema.domain.store.converter.StoreConverter;
import com.swyp.mema.domain.store.dto.naverAPI.BasicStoreRes;
import com.swyp.mema.domain.store.dto.naverAPI.BasicStoreRes.Item;
import com.swyp.mema.domain.store.dto.naverAPI.ImageRes;
import com.swyp.mema.domain.store.dto.naverAPI.StoreRes;
import com.swyp.mema.domain.store.dto.naverAPI.TotalStoreRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StoreServiceWithNaverAPI {

	private static final String BASE_URL = "https://openapi.naver.com/v1/search/local.json";

	@Value("${naver.api.client.id}")
	private String clientId;

	@Value("${naver.api.client.secret}")
	private String clientSecret;

	private final WebClient webClient;
	private final StoreConverter converter;
	private final ImageCrawler crawler;

	public StoreServiceWithNaverAPI(WebClient.Builder webClientBuilder, StoreConverter converter,
									ImageCrawler crawler) {
		this.webClient = webClientBuilder.baseUrl(BASE_URL).build(); // 기본 URL 설정
		this.converter = converter;
		this.crawler = crawler;
	}

	public TotalStoreRes search(String location) {

		// URI 빌더로 URL 생성 ("서울역" + " 맛집") 으로 파라미터 생성
		String query = location + " 맛집";
		int start = 1; // 네이버 API 페이징 값
		URI url = createUrl(query, start);

		List<StoreRes> result = new ArrayList<>();

		while (result.size() < 3) {

			// Naver 지역 API 요청
			BasicStoreRes results = requestAPI(url);

			if (results.getItems().isEmpty()) break;

			// 1. 링크가 비어있거나 중복된 링크인 경우 검증
			List<StoreRes> correctLinkRes = results.getItems().stream()
				.filter(item -> !hasMissingLinks(item))    // 1) 링크가 비어있는지 확인
				.map(converter::toStoreRes)
				.filter(
					i -> result.stream()
						.noneMatch(
							existing -> existing.getLink().equals(i.getLink())))    // 2) 중복된 link 있는지 검증
				.toList();

			// 2. 이미지 URL 크롤링 및 검증
			// -> 멀티스레드로 속도 문제 해결
			List<StoreRes> correctStoreRes = new ArrayList<>();
			for (StoreRes store : correctLinkRes) {

				ImageRes imageUrl = crawler.crawaling(store.getLink());

				if (imageUrl != null) { // 이미지가 있는 경우만 추가
					store.setImageInfo(imageUrl);
					correctStoreRes.add(store);
				} else {
					log.info("이미지를 찾을 수 없습니다 : {}", store.getName());
				}
			}

			// 최종 리스트에 추가 (최대 4개까지만)
			for (StoreRes store : correctStoreRes) {
				if (result.size() < 4) {
					result.add(store);
				} else {
					break;
				}
			}
			// 무한 루프 방지를 위한 start 증가
			start += 10; // 네이버 API 페이징 (start 값 증가)
		}
		return converter.totalStoreRes(result);
	}

	// URI 생성 메서드
	private URI createUrl(String query, int start) {

		int display = 5;	// 최대 호출 개수 5

		// 한글 또는 특수 문자 인코딩
		String encodedQuery;
		try {
			encodedQuery = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// 예외 처리: UTF-8은 항상 지원되므로 일반적으로 예외가 발생하지 않음
			throw new RuntimeException("Encoding failed", e);
		}

		// 직접 문자열 조합으로 URI 생성
		String urlString = String.format("%s?query=%s&display=%d&start=%d", BASE_URL, encodedQuery, display, start);

		// URI 객체로 변환
		URI uri = URI.create(urlString);

		System.out.println("Generated URI: " + uri);
		return uri;
	}

	// API 요청 메서드
	private BasicStoreRes requestAPI(URI uri) {
		return webClient.get()
			.uri(uri)
			.header("X-Naver-Client-Id", clientId)
			.header("X-Naver-Client-Secret", clientSecret)
			.retrieve()
			.bodyToMono(BasicStoreRes.class)
			.block();
	}

	// 누락된 링크가 있는지 확인
	private boolean hasMissingLinks(Item item) {
		return item.getLink().isEmpty();
	}
	// // 이미지 검증 메서드 추가
	// private boolean hasMissingImages(BasicStoreRes results) {
	// 	return results.getItems().stream()
	// 		.anyMatch(store -> crawler.crawaling(store.getLink()) == null);
	// }
	//
	// // 기존 데이터 중 Link 또는 이미지가 없는 경우 새로운 결과로 대체
	// private List<StoreRes> replaceWithNewDataAndImages(BasicStoreRes oldResults, BasicStoreRes newResults) {
	//
	// 	List<StoreRes> updatedResults = new ArrayList<>();
	//
	// 	int j = 0;
	// 	for (int i = 0; i < oldResults.getItems().size(); i++) {
	//
	// 		Item oldData = oldResults.getItems().get(i);
	// 		ImageRes oldDataImageLink = crawler.crawaling(oldData.getLink());
	//
	// 		// 링크가 없거나 이미지 크롤링 결과가 null이면 새 데이터로 대체
	// 		if ((oldData.getLink() == null || oldData.getLink().isEmpty()) || (oldDataImageLink == null)) {
	//
	// 			for (; j < newResults.getItems().size(); j++) {
	//
	// 				Item newData = newResults.getItems().get(j);
	// 				ImageRes newDataImageLink = crawler.crawaling(newData.getLink());
	//
	// 				if ((newData.getLink() != null && !newData.getLink().isEmpty()) && (newDataImageLink != null)) {
	//
	// 					// StoreRes 객체 생성
	// 					StoreRes storeRes = converter.toStoreRes(newData);
	// 					storeRes.setImageInfo(newDataImageLink); // 크롤링된 이미지 정보 추가
	//
	// 					updatedResults.add(storeRes); // 새로운 데이터로 대체
	// 					j++;
	// 					break;
	// 				}
	// 			}
	// 		} else {
	// 			StoreRes storeRes = converter.toStoreRes(oldData);
	// 			storeRes.setImageInfo(oldDataImageLink);
	// 			updatedResults.add(storeRes); // 기존 데이터 유지
	// 		}
	// 	}
	// 	return updatedResults;
	// }

}