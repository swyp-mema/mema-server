package com.swyp.mema.domain.charge.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PayerInfo {

    private Long memberId;  //payer의 meet member id
    private String nickname;
    private boolean isMe;

    @Builder
    public PayerInfo(Long memberId, String nickname, boolean isMe) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.isMe = isMe;
    }
}
