package com.swyp.mema.domain.meetMember.model;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.global.base.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class MeetMember extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;
	private boolean voteDateYn;
	private boolean voteLocationYn;

	@ManyToOne
	@JoinColumn(name = "meet_id", nullable = false)
	private Meet meet;

	@Builder
	public MeetMember(Meet meet, Long userId, boolean voteDateYn, boolean voteLocationYn) {
		this.meet = meet;
		this.userId = userId;
		this.voteDateYn = voteDateYn;
		this.voteLocationYn = voteLocationYn;
	}
}
