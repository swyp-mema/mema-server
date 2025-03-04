package com.swyp.mema.domain.store.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreInfo {

    private String name; // 가게 이름
    private String tel; // 전화번호
    private String category; // 카테고리

    private String roadAddress; // 도로명 주소
    private String shortAddress; // 간략 주소

    private int visitorReviewCount; // 방문자 리뷰 개수
    private int blogReviewCount;    // 블로그 리뷰 개수

    private String businessStatus;  // 현재 영업 상태
    private String businessHour;   // 오늘 영업 시간
    private String LastOrderTime;   // 라스트오더 시간

    private String menuInfo; // 대표 메뉴
    private String imageURL; // 대표 이미지

    private String latitude;    // 해당 가게 위도
    private String longitude;   // 해당 가게 경도

}
