package com.swyp.mema.domain.voteLocation.converter;

import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.domain.midlocation.dto.MidLocationDto;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.voteLocation.dto.response.MidLocationRes;
import com.swyp.mema.domain.voteLocation.dto.response.MidLocationTotalRes;
import org.junit.Test;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class LocationConverterTest {

    LocationConverter converter = new LocationConverter();

    @Test
    public void toMidLocationTotalResponse() {

        MidLocationTotalRes midLocationTotalResponse = converter.toMidLocationTotalResponse(getTestData());
        assertNotNull(midLocationTotalResponse);
    }

    public static Pair<_Station, List<MidLocationDto>> getTestData() {

        // 1) 중간역(예: 최종적으로 모일 역)
        _Station midStation = _Station.builder()
                .id(999L)
                .stationName("종로3가")
                .lineName("1호선")
                .lat("37.5711")
                .lot("127.0018")
                .address("서울특별시 종로구")
                .build();

        // 2) 유저 3명 생성 (User 엔티티에 Builder나 AllArgsConstructor가 없으므로 예시로만 작성)
        //    - 실제 테스트 코드에서는 JPA 엔티티 특성에 맞게 생성하거나 ReflectionTestUtils 등을 활용하세요.
        User user1 = new User();
        user1.setNickname("UserOne");
        user1.setPuzId(101L);
        user1.setPuzColor("#FF0000");
        // user1의 email, password, userId, role 등은 예시이므로 실제 테스트 상황에 맞게 셋업
        // ...

        User user2 = new User();
        user2.setNickname("UserTwo");
        user2.setPuzId(102L);
        user2.setPuzColor("#00FF00");
        // ...

        User user3 = new User();
        user3.setNickname("UserThree");
        user3.setPuzId(103L);
        user3.setPuzColor("#0000FF");
        // ...

        // 3) 각 유저가 지나가는 역(_Station) 4개 이상씩 준비
        //    firstStation은 "출발역" 개념으로 설정하되, 경로(path) 리스트에 포함시켜도 됩니다.

        // -- user1 의 경로 --
        _Station user1FirstStation = _Station.builder()
                .id(1L)
                .stationName("강남")
                .lineName("2호선")
                .lat("37.4979")
                .lot("127.0276")
                .address("서울시 강남구")
                .build();

        List<_Station> user1Path = Arrays.asList(
                user1FirstStation,
                _Station.builder().id(2L).stationName("역삼").lineName("2호선")
                        .lat("37.4996").lot("127.0365").address("서울시 강남구").build(),
                _Station.builder().id(3L).stationName("선릉").lineName("2호선")
                        .lat("37.5045").lot("127.0490").address("서울시 강남구").build(),
                _Station.builder().id(4L).stationName("삼성").lineName("2호선")
                        .lat("37.5089").lot("127.0632").address("서울시 강남구").build()
        );

        MidLocationDto dto1 = MidLocationDto.builder()
                .user(user1)
                .firstStation(user1FirstStation)
                .path(user1Path)
                .time(20) // 예: 이동 소요시간
                .build();

        // -- user2 의 경로 --
        _Station user2FirstStation = _Station.builder()
                .id(5L)
                .stationName("압구정")
                .lineName("3호선")
                .lat("37.5271")
                .lot("127.0285")
                .address("서울시 강남구")
                .build();

        List<_Station> user2Path = Arrays.asList(
                user2FirstStation,
                _Station.builder().id(6L).stationName("옥수").lineName("경의중앙선")
                        .lat("37.5401").lot("127.0189").address("서울시 성동구").build(),
                _Station.builder().id(7L).stationName("왕십리").lineName("2호선")
                        .lat("37.5615").lot("127.0373").address("서울시 성동구").build(),
                _Station.builder().id(8L).stationName("신당").lineName("6호선")
                        .lat("37.5656").lot("127.0175").address("서울시 중구").build(),
                _Station.builder().id(9L).stationName("동대문역사문화공원").lineName("2,4,5호선")
                        .lat("37.5651").lot("127.0079").address("서울시 중구").build()
        );

        MidLocationDto dto2 = MidLocationDto.builder()
                .user(user2)
                .firstStation(user2FirstStation)
                .path(user2Path)
                .time(25)
                .build();

        // -- user3 의 경로 --
        _Station user3FirstStation = _Station.builder()
                .id(10L)
                .stationName("혜화")
                .lineName("4호선")
                .lat("37.5822")
                .lot("127.0010")
                .address("서울시 종로구")
                .build();

        List<_Station> user3Path = Arrays.asList(
                user3FirstStation,
                _Station.builder().id(11L).stationName("동대문").lineName("1,4호선")
                        .lat("37.5713").lot("127.0094").address("서울시 종로구").build(),
                _Station.builder().id(12L).stationName("동대문역사문화공원").lineName("2,4,5호선")
                        .lat("37.5651").lot("127.0079").address("서울시 중구").build(),
                _Station.builder().id(13L).stationName("을지로4가").lineName("2,5호선")
                        .lat("37.5663").lot("126.9980").address("서울시 중구").build()
        );

        MidLocationDto dto3 = MidLocationDto.builder()
                .user(user3)
                .firstStation(user3FirstStation)
                .path(user3Path)
                .time(15)
                .build();

        // 4) MidLocationDto를 모아 Pair<_Station, List<MidLocationDto>> 생성
        List<MidLocationDto> dtoList = new ArrayList<>();
        dtoList.add(dto1);
        dtoList.add(dto2);
        dtoList.add(dto3);

        return Pair.of(midStation, dtoList);
    }
}