package com.swyp.mema.domain.voteDate.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.swyp.mema.domain.meetMember.dto.response.MeetMemberNameRes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TotalVoteDateRes {

	private LocalDate date;

	private List<MeetMemberNameRes> members;

}
