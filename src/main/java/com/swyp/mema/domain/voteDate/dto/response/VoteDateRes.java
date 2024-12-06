package com.swyp.mema.domain.voteDate.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteDateRes {

	private Long voteDateId;
	private Long meetId;
	private Long meetMemberId;
	private String meetMemberName;
	private LocalDate date;

}
