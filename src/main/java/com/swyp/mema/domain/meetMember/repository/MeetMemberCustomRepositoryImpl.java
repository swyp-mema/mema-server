package com.swyp.mema.domain.meetMember.repository;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.meet.model.QMeet;
import com.swyp.mema.domain.meetMember.dto.response.MeetMemberRes;
import com.swyp.mema.domain.meetMember.model.QMeetMember;
import com.swyp.mema.domain.user.dto.response.UserRes;
import com.swyp.mema.domain.user.model.QUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MeetMemberCustomRepositoryImpl implements MeetMemberCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<MeetMemberRes> findMeetMembersWithUserInfo(Long meetId, Long userId) {

		QMeetMember qMeetMember = QMeetMember.meetMember;
		QUser qUser = QUser.user;

		return queryFactory
			.select(
				Projections.constructor(
					MeetMemberRes.class,
					qMeetMember.id, // meetMemberId
					Expressions.booleanTemplate(
						"{0} = {1}", qUser.userId, userId // isMe 계산
					), // isMe 필드
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

	@Override
	public List<Meet> findMeetsByUserId(Long userId) {

		QMeetMember meetMember = QMeetMember.meetMember;
		QMeet meet = QMeet.meet;

		return queryFactory
			.select(meet)
			.from(meetMember)
			.join(meetMember.meet, meet)
			.where(meetMember.user.userId.eq(userId)) // userId 조건
			.distinct() // 중복 제거
			.fetch();
	}
}
