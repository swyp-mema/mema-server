package com.swyp.mema.domain.voteDate.repository;

import java.time.LocalDate;
import java.util.List;

import com.swyp.mema.domain.voteDate.dto.response.VoteDateRes;

public interface VoteDateCustomRepository {
	List<VoteDateRes> findAllByMeetId(Long meetId);
	Long countByMeetIdAndDate(Long meetId, LocalDate date);
	Long countDistinctMeetMembers();
}
