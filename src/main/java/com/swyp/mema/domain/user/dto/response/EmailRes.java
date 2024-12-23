package com.swyp.mema.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailRes {

    @Schema(description = "세션 id")
    private String sessionId;
}