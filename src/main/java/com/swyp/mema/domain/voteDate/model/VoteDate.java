package com.swyp.mema.domain.voteDate.model;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;

import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.global.base.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class VoteDate extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "meet_member_id", nullable = false) // 약속원과 연관
	private MeetMember meetMember;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private LocalDate date; // 투표 가능한 날짜

	@Builder
	public VoteDate(MeetMember meetMember, LocalDate date, User user) {
		this.meetMember = meetMember;
		this.date = date;
		this.user = user;
	}
}
