package com.swyp.mema.domain.meet.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.swyp.mema.domain.meet.model.vo.State;
import com.swyp.mema.domain.user.dto.reseponse.UserResponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetSingleResponse {

	private String meetName;
	private State meetState;
	private LocalDateTime meetDate;
	private String meetLocation;
	private LocalDateTime voteExpiredDate;
	private LocalDateTime voteExpiredLocation;

	private List<UserResponse> members;
}
