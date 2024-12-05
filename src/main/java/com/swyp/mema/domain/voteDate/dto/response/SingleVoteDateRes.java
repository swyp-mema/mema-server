package com.swyp.mema.domain.voteDate.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SingleVoteDateRes {

	private Long meetMemberId;
	private String meetMemberName;
	private List<LocalDate> date;

}
