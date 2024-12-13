package com.swyp.mema.domain.meet.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.swyp.mema.domain.user.dto.response.UserRes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MeetHomeDetailRes {

	private Long meetId;
	private int joinCode;
	private String meetName;
	private LocalDate meetDate;
	private int memberCount;
	private List<UserRes> userInfo;

}
