package com.swyp.mema.domain.meet.repository;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyp.mema.domain.meet.model.Meet;
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

	@Override
	public List<Meet> findMeetsByUserId(Long userId, int offset, int limit) {

		QMeetMember meetMember = QMeetMember.meetMember;
		QMeet meet = QMeet.meet;

		return queryFactory
			.select(meet)
			.from(meetMember)
			.join(meetMember.meet, meet)
			.where(meetMember.user.userId.eq(userId)) // userId 조건
			.distinct() // 중복 제거
			.orderBy(meet.createDate.desc()) // 정렬 조건
			.offset(offset) // 페이징 시작 위치
			.limit(limit)   // 페이징 개수
			.fetch();
	}
}
