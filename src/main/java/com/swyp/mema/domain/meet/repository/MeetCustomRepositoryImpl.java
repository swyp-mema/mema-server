package com.swyp.mema.domain.meet.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyp.mema.domain.meet.model.QMeet;
import com.swyp.mema.domain.meet.model.vo.State;
import com.swyp.mema.domain.meetMember.model.QMeetMember;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MeetCustomRepositoryImpl implements MeetCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Long countActiveMeetsByUserId(Long userId) {

		QMeet meet = QMeet.meet;
		QMeetMember meetMember = QMeetMember.meetMember;

		return queryFactory.select(meet.count())
			.from(meet)
			.join(meetMember).on(meet.id.eq(meetMember.meet.id))
			.where(
				meetMember.user.userId.eq(userId),
				meet.state.notIn(State.COMPLETED, State.SETTLING) // State 필터 조건
			)
			.fetchOne();
	}
}
