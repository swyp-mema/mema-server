package com.swyp.mema.domain.badge.controller;

import com.swyp.mema.domain.badge.dto.response.BadgeResponse;
import com.swyp.mema.domain.badge.service.BadgeService;
import com.swyp.mema.domain.charge.dto.request.ChargeReq;
import com.swyp.mema.domain.charge.service.ChargeService;
import com.swyp.mema.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "뱃지", description = "뱃지 관련 API")
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping("/mypage/badge")
    @Operation(summary = "내 뱃지정보 조회 API", description = "사용자의 뱃지 보유 현황을 조회합니다.")
    public ResponseEntity<BadgeResponse> getBadges(@AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(badgeService.getBadges());
    }
}

