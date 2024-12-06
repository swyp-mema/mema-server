package com.swyp.mema.domain.charge.controller;

import com.swyp.mema.domain.charge.dto.request.ChargeReq;
import com.swyp.mema.domain.charge.dto.response.ChargeRes;
import com.swyp.mema.domain.charge.service.ChargeService;
import com.swyp.mema.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "정산", description = "정산 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/meets/{meetId}/charge")
public class ChargeController {

    private final ChargeService chargeService;

    @PostMapping
    @Operation(summary = "정산 생성 API", description = "사용자가 결제한 금액에 대해 정산을 생성합니다.")
    public ResponseEntity<Void> createCharge(@PathVariable("meetId") Long meetId,
                                              @RequestBody ChargeReq req,
                                             @AuthenticationPrincipal  CustomUserDetails userDetails) {

        chargeService.createCharge(meetId, req, userDetails);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "정산 조회 API", description = "해당 미팅의 모든 정산을 조회합니다.")
    public ResponseEntity<List<ChargeRes>> getCharges(@PathVariable("meetId") Long meetId) {

        List<ChargeRes> res = chargeService.getCharges(meetId);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{chargeId}")
    @Operation(summary = "내 정산 수정", description = "나의 정산을 수정합니다.")
    public ResponseEntity<Void> updateCharge(@PathVariable("meetId") Long meetId,
                                             @PathVariable("chargeId") Long chargeId,
                                              @RequestBody ChargeReq req,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        chargeService.deleteCharge(meetId, chargeId);
        chargeService.createCharge(meetId, req, userDetails);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{chargeId}")
    @Operation(summary = "내 정산 삭제", description = "나의 정산을 삭제합니다.")
    public ResponseEntity<Void> deleteCharge(@PathVariable("meetId")Long meetId, @PathVariable("chargeId")Long chargeId) {

        chargeService.deleteCharge(meetId, chargeId);
        return ResponseEntity.ok().build();
    }
}
