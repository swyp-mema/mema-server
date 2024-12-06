package com.swyp.mema.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "유저 정보 변경")
public class UpdateUserInfoReq {

    @Schema(description = "닉네임", example = "메마러버")
    private String nickname;

    @Schema(description = "뱃지 모양 ID", example = "5")
    private String puzId;

    @Schema(description = "뱃지 색깔", example = "red")
    private String puzColor;
}
