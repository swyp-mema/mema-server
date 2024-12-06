package com.swyp.mema.domain.voteDate.repository;

import java.util.List;

import com.swyp.mema.domain.voteDate.dto.response.VoteDateRes;

public interface VoteDateCustomRepository {
	List<VoteDateRes> findAllByMeetId(Long meetId);
}
