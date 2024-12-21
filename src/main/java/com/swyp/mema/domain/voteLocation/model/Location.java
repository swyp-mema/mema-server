package com.swyp.mema.domain.voteLocation.model;

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
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "vote_location")
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

	private String stationName;		// 출발 위치 역이름
	private String stationRoute;	// 출발 위치 호선 정보

	private String lat;
	private String lot;

	@Builder
	public Location(Meet meet, User user, String stationName, String stationRoute, String lat, String lot) {
		this.meet = meet;
		this.user = user;
		this.stationName = stationName;
		this.stationRoute = stationRoute;
		this.lat = lat;
		this.lot = lot;
	}
}
