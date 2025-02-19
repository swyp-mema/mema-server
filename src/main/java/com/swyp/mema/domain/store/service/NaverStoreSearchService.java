package com.swyp.mema.domain.store.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.swyp.mema.domain.store.converter.StoreConverter;
import com.swyp.mema.domain.store.dto.BasicStoreRes;
import com.swyp.mema.domain.store.dto.BasicStoreRes.Item;
import com.swyp.mema.domain.store.dto.ImageRes;
import com.swyp.mema.domain.store.dto.StoreRes;
import com.swyp.mema.domain.store.dto.TotalStoreRes;

@Service
public class NaverStoreSearchService {

	private static final String BASE_URL = "https://openapi.naver.com/v1/search/local.json";

	@Value("${naver.api.client.id}")
	private String clientId;

	@Value("${naver.api.client.secret}")
	private String clientSecret;

	private final WebClient webClient;
	private final StoreConverter converter;
	private final NaverMapCrawler crawler;

	public NaverStoreSearchService(WebClient.Builder webClientBuilder, StoreConverter converter,
		NaverMapCrawler crawler) {
		this.webClient = webClientBuilder.baseUrl(BASE_URL).build(); // 기본 URL 설정
		this.converter = converter;
		this.crawler = crawler;
	}

	public TotalStoreRes search(String location) {
		// Naver API 최대 호출 횟수
		int MAX_RETRIES = 3;

		// URI 빌더로 URL 생성 ("서울역" + " 맛집") 으로 파라미터 생성
		String query = location + " 맛집";
		URI url = createUrl(query);

		// Naver API 첫 요청
		BasicStoreRes results = requestAPI(url);
		List<StoreRes> storeRes = new ArrayList<>();

		// link와 이미지 정보를 검증하고 필요하면 대체 데이터로 갱신
		int retries = 0;
		while ((hasMissingLinks(results) || hasMissingImages(results)) && retries < MAX_RETRIES) {
			System.out.println("Retrying to replace data... Attempt: " + (retries + 1));

			// 새로운 데이터 요청
			BasicStoreRes newResults = requestAPI(url);

			// Link 또는 이미지가 없는 경우 새로운 데이터로 대체
			storeRes = replaceWithNewDataAndImages(results, newResults);
			retries++;
		}

		// 최종 결과를 DTO로 변환
		// List<StoreRes> storeRes = converter.toStoreResList(results);

		// List<storeRes> -> TotalStoreRes 변환
		return converter.totalStoreRes(storeRes);
	}

	// 이미지 검증 메서드 추가
	private boolean hasMissingImages(BasicStoreRes results) {
		return results.getItems().stream()
			.anyMatch(store -> crawler.crawaling(store.getLink()) == null);
	}

	// 기존 데이터 중 Link 또는 이미지가 없는 경우 새로운 결과로 대체
	private List<StoreRes> replaceWithNewDataAndImages(BasicStoreRes oldResults, BasicStoreRes newResults) {

		List<StoreRes> updatedResults = new ArrayList<>();

		int j = 0;
		for (int i = 0; i < oldResults.getItems().size(); i++) {

			Item oldData = oldResults.getItems().get(i);
			ImageRes oldDataImageLink = crawler.crawaling(oldData.getLink());

			// 링크가 없거나 이미지 크롤링 결과가 null이면 새 데이터로 대체
			if ((oldData.getLink() == null || oldData.getLink().isEmpty()) || (oldDataImageLink == null)) {

				for (; j < newResults.getItems().size(); j++) {

					Item newData = newResults.getItems().get(j);
					ImageRes newDataImageLink = crawler.crawaling(newData.getLink());

					if ((newData.getLink() != null && !newData.getLink().isEmpty()) && (newDataImageLink != null)) {

						// StoreRes 객체 생성
						StoreRes storeRes = converter.toStoreRes(newData);
						storeRes.setImageInfo(newDataImageLink); // 크롤링된 이미지 정보 추가

						updatedResults.add(storeRes); // 새로운 데이터로 대체
						j++;
						break;
					}
				}
			} else {
				StoreRes storeRes = converter.toStoreRes(oldData);
				storeRes.setImageInfo(oldDataImageLink);
				updatedResults.add(storeRes); // 기존 데이터 유지
			}
		}
		return updatedResults;
	}

	// URI 생성 메서드
	private URI createUrl(String query) {
		int display = 4;
		int start = 1;

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
	private boolean hasMissingLinks(BasicStoreRes results) {
		return results.getItems().stream()
			.anyMatch(store -> store.getLink() == null || store.getLink().isEmpty());
	}
}