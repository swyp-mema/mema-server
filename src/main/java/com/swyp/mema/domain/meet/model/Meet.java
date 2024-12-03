package com.swyp.mema.domain.meet.model;

import static jakarta.persistence.GenerationType.*;

import java.time.LocalDateTime;

import com.swyp.mema.domain.meet.model.vo.State;
import com.swyp.mema.global.base.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
	@Column(nullable = false)
	private int code;

	@Column(nullable = false, length = 20)
	private String name;

	@Enumerated(EnumType.STRING)
	private State state;

	private LocalDateTime meetDate;
	private String location;
	private LocalDateTime expiredVoteDate;
	private LocalDateTime expiredVoteLocation;

	@Builder
	public Meet(int code, String name, State state, LocalDateTime meetDate, String location,
		LocalDateTime expiredVoteDate, LocalDateTime expiredVoteLocation) {
		this.code = code;
		this.name = name;
		this.state = state;
		this.meetDate = meetDate;
		this.location = location;
		this.expiredVoteDate = expiredVoteDate;
		this.expiredVoteLocation = expiredVoteLocation;
	}

	public void changeName(String meetName) {

		// 검증 로직
		if (meetName == null || meetName.isBlank()) {
			throw new IllegalArgumentException("약속명은 비어 있을 수 없습니다.");
		}
		if (meetName.length() > 20) {
			throw new IllegalArgumentException("약속명은 20자 이하로 입력해주세요.");
		}
		this.name = meetName;
	}
}
