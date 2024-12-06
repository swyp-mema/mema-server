package com.swyp.mema.domain.meetMember.model;

import java.util.ArrayList;
import java.util.List;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.voteDate.model.VoteDate;
import com.swyp.mema.global.base.domain.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class MeetMember extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private boolean voteDateYn;
	private boolean voteLocationYn;

	@ManyToOne
	@JoinColumn(name = "meet_id", nullable = false)
	private Meet meet;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(mappedBy = "meetMember", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VoteDate> dateVotes = new ArrayList<>();

	@Builder
	public MeetMember(Meet meet, User user, boolean voteDateYn, boolean voteLocationYn) {
		this.meet = meet;
		this.user = user;
		this.voteDateYn = voteDateYn;
		this.voteLocationYn = voteLocationYn;
	}

	public void setMeet(Meet meet) {
		this.meet = meet;
	}
}
