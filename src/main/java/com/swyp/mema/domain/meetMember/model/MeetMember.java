package com.swyp.mema.domain.meetMember.model;

import com.swyp.mema.global.base.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class MeetMember extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long meetId;
	private Long userId;
	private boolean voteDateYn;
	private boolean voteLocationYn;

	@Builder
	public MeetMember(Long meetId, Long userId, boolean voteDateYn, boolean voteLocationYn) {
		this.meetId = meetId;
		this.userId = userId;
		this.voteDateYn = voteDateYn;
		this.voteLocationYn = voteLocationYn;
	}
}
