package com.swyp.mema.domain.meetMember.repository;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyp.mema.domain.meetMember.model.QMeetMember;
import com.swyp.mema.domain.user.dto.reseponse.UserRes;
import com.swyp.mema.domain.user.model.QUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MeetMemberCustomRepositoryImpl implements MeetMemberCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<UserRes> findMeetMembersWithUserInfo(Long meetId) {

		QMeetMember qMeetMember = QMeetMember.meetMember;
		QUser qUser = QUser.user;

		return queryFactory
			.select(Projections.constructor(
				UserRes.class,
				qUser.userId,        // userId
				qUser.nickname,      // nickname
				qUser.puzId,         // puzzleId
				qUser.puzColor       // puzzleColor
			))
			.from(qMeetMember)
			.join(qUser).on(qMeetMember.userId.eq(qUser.userId))
			.where(qMeetMember.meet.id.eq(meetId))
			.fetch();
	}
}
