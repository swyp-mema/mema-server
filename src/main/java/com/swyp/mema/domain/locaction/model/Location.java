package com.swyp.mema.domain.locaction.model;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.swyp.mema.domain.meet.model.Meet;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.global.base.domain.BaseEntity;

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
public class Location extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "meet_id", nullable = false)
	private Meet meet;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private String stationId;		// 출발 위치 역ID
	private String stationName;		// 출발 위치 역이름
	private String stationRoute;	// 출발 위치 호선 정보

	@Builder
	public Location(Meet meet, User user, String stationId, String stationName, String stationRoute) {
		this.meet = meet;
		this.user = user;
		this.stationId = stationId;
		this.stationName = stationName;
		this.stationRoute = stationRoute;
	}
}
