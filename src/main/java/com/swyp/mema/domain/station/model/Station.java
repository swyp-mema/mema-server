package com.swyp.mema.domain.station.model;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.swyp.mema.global.base.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Immutable
public class Station extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String stationName;	// 역이름

	@Column(nullable = false)
	private String lineName;	// 호선 정보

	private int waitTime;		// 배차 간격

	private int toNext;			// 다음역까지 소요시간

	private String lat;	// 위도

	private String lot;	// 경도

	@Builder
	public Station(String stationName, String lineName, String lat, String lot) {
		this.stationName = stationName;
		this.lineName = lineName;
		this.lat = lat;
		this.lot = lot;
	}
}
