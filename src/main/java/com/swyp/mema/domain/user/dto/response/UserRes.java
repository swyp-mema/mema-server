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
@Schema(description = "유저 프로필 응답")
public class UserRes {

	@Schema(description = "유저 고유 ID", example = "1")
	private Long userId;

	@Schema(description = "닉네임", example = "메마러버")
	private String nickname;

	@Schema(description = "뱃지 모양 ID", example = "5")
	private Long puzzleId;

	@Schema(description = "뱃지 색깔", example = "red")
	private String puzzleColor;
}
