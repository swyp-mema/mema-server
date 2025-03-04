package com.swyp.mema.domain.store.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp.mema.domain.store.dto.StoreInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceWithNaverMap {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StoreServiceWithNaverMap() {
        this.webClient = WebClient.builder()
                .baseUrl("https://map.naver.com")
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Referer", "https://map.naver.com/") // 네이버 내부 요청처럼 보이게
                .defaultHeader("Cookie", "NID_AUT=xxx; NID_SES=xxx;") // 필요시 네이버 쿠키 추가
                .build();
    }

    public List<StoreInfo> getStoreInfo(String query) {

        String searchCoord = "127.0048;37.5063"; // 고속터미널 위경도
        String endpoint = "/p/api/search/allSearch?query=" + query + " 맛집" + "&type=all&searchCoord="+searchCoord;

        // WebClient로 네이버 API 호출
        Mono<String> response = webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(String.class);

        // JSON 응답을 DTO로 변환
        String jsonResponse = response.block(); // 동기 방식으로 데이터 가져오기
        return parseJson(jsonResponse);
    }

    private List<StoreInfo> parseJson(String jsonResponse) {

        List<StoreInfo> stores = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode placeList = root.path("result").path("place").path("list");

            int count = 0;
            for (JsonNode placeNode : placeList) {

                StoreInfo store = new StoreInfo();

                store.setName(placeNode.path("name").asText());
                store.setTel(placeNode.path("tel").asText());

                store.setRoadAddress(placeNode.path("roadAddress").asText());   // 도로명 주소
                store.setShortAddress(placeNode.path("abbrAddress").asText());  // 짧은 주소

                store.setVisitorReviewCount(placeNode.path("placeReviewCount").asInt());   // 방문자 리뷰
                store.setBlogReviewCount(placeNode.path("reviewCount").asInt());        // 블로그 리뷰

                JsonNode businessStatusNode = placeNode.path("businessStatus");
                JsonNode statusNode = businessStatusNode.path("status");
                String businessHours = businessStatusNode.path("businessHours").asText();

                store.setBusinessStatus(statusNode.path("text").asText()); // 현재 영업 상태
                store.setBusinessHour(businessHours);   // 오늘 영업 시간
                store.setLastOrderTime(statusNode.path("detailInfo").asText()); // 라스트오더 시간

                store.setMenuInfo(placeNode.path("menuInfo").asText());
                store.setImageURL(placeNode.path("thumUrl").asText());   // 이미지 링크

                store.setLatitude(placeNode.path("x").asText()); // 위도
                store.setLongitude(placeNode.path("y").asText()); // 경도

                JsonNode categoryNode = placeNode.path("category");
                String categoryResult = "";

                // category 배열이 존재하는 경우
                if (categoryNode.isArray() && !categoryNode.isEmpty()) {
                    if (categoryNode.size() > 1) {
                        // 1번째 인덱스 값이 존재하면 해당 값 사용
                        categoryResult = categoryNode.get(1).asText();
                    } else {
                        // 1번째 인덱스가 없으면 0번째 인덱스 값 사용
                        categoryResult = categoryNode.get(0).asText();
                    }
                }

                store.setCategory(categoryResult);  // 카테고리

                stores.add(store);
                count++;

                if (count == 4) break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stores;
    }
}
