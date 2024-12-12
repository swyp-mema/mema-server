package com.swyp.mema.domain.meet.model;

import static jakarta.persistence.GenerationType.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.swyp.mema.domain.meet.exception.MeetNameOverLengthException;
import com.swyp.mema.domain.meet.exception.NotEmptyMeetNameException;
import com.swyp.mema.domain.meet.model.vo.State;
import com.swyp.mema.domain.meetMember.model.MeetMember;
import com.swyp.mema.global.base.domain.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Meet extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Min(value = 100000, message = "코드는 최소 6자리여야 합니다.") // 최소값
	@Max(value = 999999, message = "코드는 최대 6자리여야 합니다.") // 최대값
	@Column(nullable = false, unique = true)
	private int code;

	@Column(nullable = false, length = 20)
	private String name;

	@Enumerated(EnumType.STRING)
	private State state;

	private LocalDate meetDate;
	private String meetLocation;
	private LocalDateTime expiredVoteDate;

	@OneToMany(mappedBy = "meet", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<MeetMember> members = new ArrayList<>();

	@Builder
	public Meet(int code, String name, State state, LocalDate meetDate, String meetLocation,
		LocalDateTime expiredVoteDate) {
		this.code = code;
		this.name = name;
		this.state = state;
		this.meetDate = meetDate;
		this.meetLocation = meetLocation;
		this.expiredVoteDate = expiredVoteDate;
	}

	public void addMember(MeetMember member) {
		members.add(member);
		member.setMeet(this);
	}

	public void changeName(String meetName) {
		// 검증 로직
		if (meetName == null || meetName.isBlank()) {
			throw new NotEmptyMeetNameException();
		}
		if (meetName.length() > 20) {
			throw new MeetNameOverLengthException();
		}
		this.name = meetName;
	}

	public void changeState(State state) {
		this.state = state;
	}

	public void setExpiredVoteDate(LocalDateTime dateTime) {
		this.expiredVoteDate = dateTime;
	}

	public void setMeetDate(LocalDate dateTime) {this.meetDate = dateTime; }
}
