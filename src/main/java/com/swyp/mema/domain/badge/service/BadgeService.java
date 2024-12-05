package com.swyp.mema.domain.badge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BadgeService {

    // 보유 뱃지수 조회
    public int getBadgeCount(){
        return 1;
    }
}
