package com.swyp.mema.domain.midlocation.dto;

import com.swyp.mema.database.station.model._Station;
import com.swyp.mema.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MidLocationDto {
    /**
     * 중간역 계산 결과를 Location service에 반환해주는 response DTO
     */
    User user;
    _Station firstStation;
    List<_Station> path;
    Integer time;
}
