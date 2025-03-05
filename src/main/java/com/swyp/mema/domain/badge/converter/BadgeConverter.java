package com.swyp.mema.domain.badge.converter;

import com.swyp.mema.domain.badge.dto.response.BadgeRes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class BadgeConverter {

    public BadgeRes toBadgeResponse(ArrayList<Boolean> badgeStatus) {


        // BadgeResponse 객체를 생성하여 반환합니다.
        return BadgeRes.builder()
                .badgeList(badgeStatus)
                .build();
    }
}
