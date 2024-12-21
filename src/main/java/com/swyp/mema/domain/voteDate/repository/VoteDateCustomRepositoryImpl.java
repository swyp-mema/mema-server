package com.swyp.mema.domain.voteDate.repository;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
					qMeetMember.id,	// meetMemberId 반환
					qMeetMember.user.nickname,
					qVoteDate.date
				)
			)
			.from(qVoteDate)
			.join(qVoteDate.meetMember, qMeetMember)
			.where(qMeetMember.meet.id.eq(meetId))
			.fetch();
	}

	@Override
	public Long countByMeetIdAndDate(Long meetId, LocalDate date) {

		QVoteDate voteDate = QVoteDate.voteDate;

		return queryFactory
			.select(voteDate.count())
			.from(voteDate)
			.where(
				voteDate.meetMember.meet.id.eq(meetId),
				voteDate.date.eq(date)
			)
			.fetchOne();
	}

	@Override
	public Long countDistinctMeetMembers() {
		QVoteDate voteDate = QVoteDate.voteDate;

		return queryFactory
			.select(voteDate.meetMember.id.countDistinct()) // meetMemberId의 중복을 제거 후 카운트
			.from(voteDate)
			.fetchOne(); // 단일 값 반환
	}
}
