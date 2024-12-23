package com.swyp.mema.domain.badge.converter;

import com.swyp.mema.domain.badge.dto.response.BadgeResponse;
import com.swyp.mema.domain.badge.model.Badge;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class BadgeConverter {

    public BadgeResponse toBadgeResponse(ArrayList<Boolean> badgeStatus) {


        // BadgeResponse 객체를 생성하여 반환합니다.
        return BadgeResponse.builder()
                .badgeList(badgeStatus)
                .build();
    }
}
