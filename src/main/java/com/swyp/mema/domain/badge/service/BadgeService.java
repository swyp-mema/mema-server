package com.swyp.mema.domain.badge.service;

import com.swyp.mema.domain.badge.converter.BadgeConverter;
import com.swyp.mema.domain.badge.dto.response.BadgeRes;
import com.swyp.mema.domain.badge.model.Badge;
import com.swyp.mema.domain.badge.repository.BadgeRepository;
import com.swyp.mema.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;
    private final BadgeConverter badgeConverter;

    // 보유 뱃지수 조회
    public int getBadgeCount(){

        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        Badge badge = badgeRepository.findByUser(userRepository.findByUserId(userId));

        int badgeCount = 0;
        for (boolean flag : badge.getAllBadges()) {

            if (flag) badgeCount++;
        }
        return badgeCount;
    }

    // 보유 뱃지 현황 조회
    public BadgeRes getBadges(){

        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        Badge badge = badgeRepository.findByUser(userRepository.findByUserId(userId));
        return badgeConverter.toBadgeResponse(badge.getAllBadges());
    }
}
