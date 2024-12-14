package com.swyp.mema.domain.station.model;

import static lombok.AccessLevel.*;

import com.swyp.mema.global.base.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Station extends BaseEntity { // + 위/경도

	@Id
	@Column(unique = true, nullable = false)
	private String stationId;	// 역 ID (PK)

	@Column(nullable = false)
	private String stationName;	// 역이름

	@Column(nullable = false)
	private String routeName;	// 호선 정보

	private int waitTime;		// 배차 간격

	private int toNext;			// 다음역까지 소요시간

	private double latitude;	// 위도

	private double longitude;	// 경도

	@Builder
	public Station(String stationId, String stationName, String routeName) {
		this.stationId = stationId;
		this.stationName = stationName;
		this.routeName = routeName.equals("공항") ? "공항철도" : routeName;
	}
}
