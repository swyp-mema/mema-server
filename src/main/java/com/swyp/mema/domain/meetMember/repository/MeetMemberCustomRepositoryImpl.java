package com.swyp.mema.domain.meetMember.repository;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyp.mema.domain.meetMember.model.QMeetMember;
import com.swyp.mema.domain.user.dto.reseponse.UserResponse;
import com.swyp.mema.domain.user.model.QUsers;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MeetMemberCustomRepositoryImpl implements MeetMemberCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<UserResponse> findMeetMembersWithUserInfo(Long meetId) {

		QMeetMember qMeetMember = QMeetMember.meetMember;
		QUsers qUser = QUsers.users;

		return queryFactory
			.select(Projections.constructor(
				UserResponse.class,
				qUser.userId,        // userId
				qUser.nickname,      // nickname
				qUser.puzId,         // puzzleId
				qUser.puzColor       // puzzleColor
			))
			.from(qMeetMember)
			.join(qUser).on(qMeetMember.userId.eq(qUser.userId))
			.where(qMeetMember.meetId.eq(meetId))
			.fetch();
	}
}
