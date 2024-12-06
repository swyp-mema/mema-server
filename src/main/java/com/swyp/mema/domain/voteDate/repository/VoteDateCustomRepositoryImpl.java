package com.swyp.mema.domain.voteDate.repository;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyp.mema.domain.meetMember.dto.response.MeetMemberRes;
import com.swyp.mema.domain.meetMember.model.QMeetMember;
import com.swyp.mema.domain.voteDate.dto.response.VoteDateRes;
import com.swyp.mema.domain.voteDate.model.QVoteDate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VoteDateCustomRepositoryImpl implements VoteDateCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<VoteDateRes> findAllByMeetId(Long meetId) {
		QVoteDate qVoteDate = QVoteDate.voteDate;
		QMeetMember qMeetMember = QMeetMember.meetMember;

		return queryFactory
			.select(
				Projections.constructor(
					VoteDateRes.class,
					qVoteDate.id,
					qMeetMember.meet.id,
					qMeetMember.user.userId,
					qMeetMember.user.nickname,
					qVoteDate.date
				)
			)
			.from(qVoteDate)
			.join(qVoteDate.meetMember, qMeetMember)
			.where(qMeetMember.meet.id.eq(meetId))
			.fetch();
	}
}
