package com.swyp.mema.domain.charge.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChargeRes {

    private Long chargeId;              // 정산 ID
    private String content;             // 정산 내용
    private Integer totalPrice;             // 총 금액
    private Integer peopleNumber;           // 정산 총 인원수
    private String payeeNickname;       // 정산자 닉네임
    private List<PayerInfo> members;     // 피정산자 정보 리스트

    @Builder
    public ChargeRes(Long chargeId, String content, int totalPrice, int peopleNumber, String payeeNickname, List<PayerInfo> members) {
        this.chargeId = chargeId;
        this.content = content;
        this.totalPrice = totalPrice;
        this.peopleNumber = peopleNumber;
        this.payeeNickname = payeeNickname;
        this.members = members;
    }
}
