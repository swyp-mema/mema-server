package com.swyp.mema.domain.user.dto.converter;

import com.swyp.mema.domain.badge.service.BadgeService;
import com.swyp.mema.domain.user.dto.response.UserInfoRes;
import com.swyp.mema.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final BadgeService badgeService;

    public UserInfoRes user2UserInfoRes(User user) {

        int meetCount, badgeCount;
        meetCount = 0;
        badgeCount = badgeService.getBadgeCount();

        return UserInfoRes.builder()
                .nickname(user.getNickname())
                .puzzleId(user.getPuzId())
                .puzzleColor(user.getPuzColor())
                .role(user.getRole())
                .visitCount(user.getVisitCount())
                .meetCount(meetCount)
                .badgeCount(badgeCount)
                .build();

    }
}