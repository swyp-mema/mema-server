package com.swyp.mema.domain.voteLocation.service;

import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.domain.midlocation.dto.MidLocationDto;
import com.swyp.mema.domain.midlocation.service.MidLocationService;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.voteLocation.converter.LocationConverter;
import com.swyp.mema.domain.voteLocation.dto.response.MidLocationTotalRes;
import com.swyp.mema.domain.voteLocation.model.Location;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
class LocationServiceTest {

    @Autowired private LocationService locationService;
    @Autowired
    private MidLocationService midLocationService;
    @Autowired
    private LocationConverter locationConverter;

    @Test
    void getTotalLocation() {

        User user1 = User.builder()
                .email("user1@example.com")
                .password("pass1")
                .nickname("유저1")
                .puzId(1L)
                .puzColor("blue")
                .role("ROLE_USER")
                .visitCount(1)
                .build();

        User user2 = User.builder()
                .email("user2@example.com")
                .password("pass2")
                .nickname("유저2")
                .puzId(2L)
                .puzColor("green")
                .role("ROLE_USER")
                .visitCount(1)
                .build();

        User user3 = User.builder()
                .email("user3@example.com")
                .password("pass3")
                .nickname("유저3")
                .puzId(3L)
                .puzColor("red")
                .role("ROLE_USER")
                .visitCount(1)
                .build();

        User user4 = User.builder()
                .email("user4@example.com")
                .password("pass4")
                .nickname("유저4")
                .puzId(4L)
                .puzColor("yellow")
                .role("ROLE_USER")
                .visitCount(1)
                .build();

        Location location1 = Location.builder()
                .meet(null)
                .user(user1)
                .stationName("청명")
                .stationRoute("수인분당선")
                .lat("37.555113")
                .lot("126.970678")
                .build();

        Location location2 = Location.builder()
                .meet(null)
                .user(user2)
                .stationName("부평")
                .stationRoute("1호선")
                .lat("37.557192")
                .lot("126.925381")
                .build();

        Location location3 = Location.builder()
                .meet(null)
                .user(user3)
                .stationName("강남")
                .stationRoute("2호선")
                .lat("37.497175")
                .lot("127.027926")
                .build();

        Location location4 = Location.builder()
                .meet(null)
                .user(user4)
                .stationName("답십리")
                .stationRoute("5호선")
                .lat("37.513950")
                .lot("127.102234")
                .build();


        Pair<_Station, List<MidLocationDto>> totalMidStation = midLocationService.getTotalMidStation(Arrays.asList(location1, location2, location3, location4));
        MidLocationTotalRes midLocationTotalResponse = locationConverter.toMidLocationTotalResponse(totalMidStation);
        midLocationTotalResponse.printAll();
    }
}