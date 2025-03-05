package com.swyp.mema.domain.badge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class BadgeRes {

    @Schema(description = "뱃지 1", example = "true")
    private boolean badge1;

    @Schema(description = "뱃지 2", example = "false")
    private boolean badge2;

    @Schema(description = "뱃지 3", example = "true")
    private boolean badge3;

    @Schema(description = "뱃지 4", example = "false")
    private boolean badge4;

    @Schema(description = "뱃지 5", example = "true")
    private boolean badge5;

    @Schema(description = "뱃지 6", example = "true")
    private boolean badge6;

    @Schema(description = "뱃지 7", example = "true")
    private boolean badge7;

    @Schema(description = "뱃지 8", example = "true")
    private boolean badge8;

    @Schema(description = "뱃지 9", example = "true")
    private boolean badge9;

    @Schema(description = "뱃지 10", example = "true")
    private boolean badge10;

    @Schema(description = "뱃지 11", example = "true")
    private boolean badge11;

    @Schema(description = "뱃지 12", example = "true")
    private boolean badge12;

    @Builder
    public BadgeRes(ArrayList<Boolean> badgeList) {

        if (badgeList.size() != 12) {
            throw new IllegalArgumentException("badgeList must contain exactly 12 elements.");
        }

        this.badge1 = badgeList.get(0);
        this.badge2 = badgeList.get(1);
        this.badge3 = badgeList.get(2);
        this.badge4 = badgeList.get(3);
        this.badge5 = badgeList.get(4);
        this.badge6 = badgeList.get(5);
        this.badge7 = badgeList.get(6);
        this.badge8 = badgeList.get(7);
        this.badge9 = badgeList.get(8);
        this.badge10 = badgeList.get(9);
        this.badge11 = badgeList.get(10);
        this.badge12 = badgeList.get(11);
    }
}
