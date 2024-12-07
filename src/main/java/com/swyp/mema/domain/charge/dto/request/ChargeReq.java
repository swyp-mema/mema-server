package com.swyp.mema.domain.charge.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "정산 생성")
public class ChargeReq {

    // 정산 내용
    @Schema(description = "정산 내용", example = "낭만오댕")
//    @Size(min = 1, max = 20, message = "약속명은 1자 이상 20자 이하로 입력해주세요.") // 길이 제한
    private String content;

    // 총 금액
    @Schema(description = "정산 금액", example = "55000")
//    @NotBlank(message = "정산 금액을 입력해주세요.")
    private int totalPrice;

    // 정산 총 인원 수 (정산자 본인 포함)
    @Schema(description = "정산 인원 수 (본인 포함)", example = "4")
//    @NotBlank(message = "정산 인원을 입력해주세요.")
    private int peopleNumber;

    // 피정산자 아이디 목록 (정산자 아이디 제외)
    @Schema(description = "피정산자 meet member id 목록 (정산자 본인 제외)", example = "[152,53,26,44]")
    @NotBlank(message = "정산 대상자를 입력해주세요.")
    private List<Long> payerIds;
}
