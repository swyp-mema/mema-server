package com.swyp.mema.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "유저 상세정보 (마이페이지 메인)")
public class UserInfoRes {

    @Schema(description = "닉네임", example = "메마러버")
    private String nickname;

    @Schema(description = "뱃지 모양 ID", example = "5")
    private Long puzzleId;

    @Schema(description = "뱃지 색깔", example = "red")
    private String puzzleColor;

    @Schema(description = "회원 타입", example = "ROLE_NAVER")
    private String role;

    @Schema(description = "방문 횟수", example = "23")
    private Integer visitCount;

    @Schema(description = "미팅 참여 횟수", example = "5")
    private Integer meetCount;

    @Schema(description = "보유 뱃지 수", example = "4")
    private Integer badgeCount;
}