package com.swyp.mema.domain.charge.dto.response;

import lombok.Builder;

public class PayerInfo {

    private Long payerId;
    private String payerNickname;

    @Builder
    public PayerInfo(Long payerId, String payerNickname) {
        this.payerId = payerId;
        this.payerNickname = payerNickname;
    }
}
