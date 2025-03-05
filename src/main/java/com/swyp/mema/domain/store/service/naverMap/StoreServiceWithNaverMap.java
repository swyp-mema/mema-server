package com.swyp.mema.domain.store.service.naverMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.database.station.repository.StationRepository;
import com.swyp.mema.domain.store.dto.naverMap.StoreInfoRes;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreServiceWithNaverMap {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final StationRepository stationRepository;

    public StoreServiceWithNaverMap(StationRepository stationRepository) {

        this.objectMapper= new ObjectMapper();
        this.stationRepository = stationRepository;

        this.webClient = WebClient.builder()
                .baseUrl("https://map.naver.com")
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Referer", "https://map.naver.com/") // 네이버 내부 요청처럼 보이도록 설정
                .defaultHeader("Cookie", "NID_AUT=xxx; NID_SES=xxx;") // 네이버 쿠키 추가
                .build();
    }

    public List<StoreInfoRes> getStoreInfo(String meetStationName) {

        // _station 테이블에서 latitude, longitude 조회
        // 수정될 예정
        _Station station = stationRepository.findByStationName(meetStationName).getFirst();

        String stationLocation = station.getLot() + ";" + station.getLat();

        // "역"으로 끝나지 않으면 추가 (예 : meetStationName == 서울 => 서울역 맛집)
        if (!meetStationName.endsWith("역")) {
            meetStationName += "역";
        }

        String endpoint = "/p/api/search/allSearch?query=" + meetStationName + " 맛집" + "&type=all&searchCoord="+stationLocation;

        // WebClient로 네이버 API 호출
        Mono<String> response = webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(String.class);

        // JSON 응답을 DTO로 변환
        String jsonResponse = response.block(); // 동기 방식으로 데이터 가져오기
        return parseJson(jsonResponse);
    }

    private List<StoreInfoRes> parseJson(String jsonResponse) {

        List<StoreInfoRes> stores = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode placeNode = root.path("result").path("place").path("list");

            int count = 0;
            for (JsonNode place : placeNode) {

                StoreInfoRes storeInfo = new StoreInfoRes();

                storeInfo.setName(place.path("name").asText());
                storeInfo.setTel(place.path("tel").asText());

                storeInfo.setRoadAddress(place.path("roadAddress").asText());   // 도로명 주소
                storeInfo.setShortAddress(place.path("abbrAddress").asText());  // 짧은 주소

                storeInfo.setVisitorReviewCount(place.path("placeReviewCount").asInt());   // 방문자 리뷰
                storeInfo.setBlogReviewCount(place.path("reviewCount").asInt());        // 블로그 리뷰

                JsonNode businessStatusNode = place.path("businessStatus");
                JsonNode statusNode = businessStatusNode.path("status");
                String businessHours = businessStatusNode.path("businessHours").asText();

                storeInfo.setBusinessStatus(statusNode.path("text").asText()); // 현재 영업 상태
                storeInfo.setBusinessHour(businessHours);   // 오늘 영업 시간
                storeInfo.setLastOrderTime(statusNode.path("detailInfo").asText()); // 라스트오더 시간

                storeInfo.setMenuInfo(place.path("menuInfo").asText());
                storeInfo.setImageURL(place.path("thumUrl").asText());   // 이미지 링크

                storeInfo.setLatitude(place.path("x").asText()); // 위도
                storeInfo.setLongitude(place.path("y").asText()); // 경도

                JsonNode categoryNode = place.path("category");
                String category = "";

                // category 배열이 존재하는 경우
                if (categoryNode.isArray() && !categoryNode.isEmpty()) {
                    category = categoryNode.size() > 1 ? categoryNode.get(1).asText() : categoryNode.get(0).asText();
                }

                storeInfo.setCategory(category);  // 카테고리

                stores.add(storeInfo);
                count++;

                if (count == 4) break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stores;
    }
}
