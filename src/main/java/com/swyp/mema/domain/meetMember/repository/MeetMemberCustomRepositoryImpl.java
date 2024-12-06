package com.swyp.mema.domain.meetMember.repository;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyp.mema.domain.meetMember.dto.response.MeetMemberRes;
import com.swyp.mema.domain.meetMember.model.QMeetMember;
import com.swyp.mema.domain.user.dto.response.UserRes;
import com.swyp.mema.domain.user.model.QUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MeetMemberCustomRepositoryImpl implements MeetMemberCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<MeetMemberRes> findMeetMembersWithUserInfo(Long meetId) {

		QMeetMember qMeetMember = QMeetMember.meetMember;
		QUser qUser = QUser.user;

		return queryFactory
			.select(
				Projections.constructor(
					MeetMemberRes.class,
					qMeetMember.id, // meetMemberId
					Projections.constructor(
						UserRes.class, // 중첩된 UserRes 매핑
						qUser.userId,
						qUser.nickname,
						qUser.puzId,
						qUser.puzColor
					)
				)
			)
			.from(qMeetMember)
			.join(qUser).on(qMeetMember.user.userId.eq(qUser.userId))
			.where(qMeetMember.meet.id.eq(meetId))
			.fetch();
	}
}
