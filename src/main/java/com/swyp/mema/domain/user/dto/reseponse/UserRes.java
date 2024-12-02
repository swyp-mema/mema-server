package com.swyp.mema.domain.user.dto.reseponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "유저 응답")
public class UserRes {

	@Schema(description = "유저 Id", example = "1")
	private Long userId;

	@Schema(description = "닉네임", example = "메마러버")
	private String nickname;

	@Schema(description = "퍼즐 Id", example = "5")
	private Long puzzleId;

	@Schema(description = "퍼즐 배경 색깔", example = "red")
	private String puzzleColor;
}
