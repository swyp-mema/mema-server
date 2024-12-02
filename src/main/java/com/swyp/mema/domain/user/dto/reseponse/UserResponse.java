package com.swyp.mema.domain.user.dto.reseponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

	private Long userId;
	private String nickname;
	private Long puzzleId;
	private String puzzleColor;
}
