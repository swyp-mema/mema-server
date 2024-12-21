package com.swyp.mema.domain.charge.controller;

import com.swyp.mema.domain.charge.dto.request.ChargeReq;
import com.swyp.mema.domain.charge.dto.response.ChargeRes;
import com.swyp.mema.domain.charge.service.ChargeService;
import com.swyp.mema.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meets/{meetId}/charge")
@Tag(name = "정산", description = "정산 관련 API")
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

    @GetMapping("/total")
    @Operation(summary = "정산 조회 API", description = "해당 미팅의 모든 정산을 조회합니다.")
    public ResponseEntity<List<ChargeRes>> getCharges(@PathVariable("meetId") Long meetId) {

        List<ChargeRes> res = chargeService.getCharges(meetId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/payfor")
    @Operation(summary = "내 피정산 조회 API", description = "사용자가 돈을 지불해야 하는 정산들을 조회합니다.")
    public ResponseEntity<List<ChargeRes>> getPayCharges(@PathVariable("meetId") Long meetId,
                                                         @AuthenticationPrincipal  CustomUserDetails userDetails) {

        List<ChargeRes> res = chargeService.getPayCharges(meetId, userDetails);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{chargeId}")
    @Operation(summary = "정산 개별 조회", description = "정산을 조회합니다.")
    public ResponseEntity<List<ChargeRes>> getCharge(@PathVariable("meetId") Long meetId,
                                             @PathVariable("chargeId") Long chargeId) {

        List<ChargeRes> res = chargeService.getCharge(meetId, chargeId);
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

        System.out.println("Charge deleted");

        chargeService.deleteCharge(meetId, chargeId);
        return ResponseEntity.ok().build();
    }
}
