package com.swyp.mema.domain.station.dto.response.nearSubway;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NearSubwayResponse {

	// 순서
	private int rowNum;

	/**
	 * 지하철호선ID
	 *  (1001:1호선, 1002:2호선, 1003:3호선, 1004:4호선, 1005:5호선 1006:6호선, 1007:7호선, 1008:8호선, 1009:9호선,
	 *  1061:중앙선1063:경의중앙선, 1065:공항철도, 1067:경춘선, 1075:수의분당선 1077:신분당선, 1092:우이신설선, 1093:서해선, 1081:경강선, 1032:GTX-A)
	 */
	private String subwayLine;

	// 이전 지하철역 ID
	private String statnFid;

	// 다음 지하철역ID
	private String statnTid;

	// 지하철역ID
	private String statnId;

	// 지하철역명
	private String statnNm;

	// 환승노선수
	private String trnsitCo;

	/**
	 * 도착예정열차순번
	 * (상하행코드(1자리), 순번(첫번째, 두번째 열차 , 1자리), 첫번째 도착예정 정류장 - 현재 정류장(3자리), 목적지 정류장, 급행여부(1자리))
	 * Ex. 왕십리 => 01000방화0
	 */
	private String ordkey;

	/**
	 * 연계호선ID (1002, 1007 등 연계대상 호선ID)
	 */
	private String subwayList;

	/**
	 * 연계지하철역ID (1002000233, 1007000000)
	 */
	private String statnList;

	/**
	 * 열차종류 (급행,ITX,일반,특급)
	 */
	private String btrainSttus;

	// 열차도착 예정시간 (단위:초)
	private String barvlDt;

	// 열차번호 (현재운행하고 있는 호선별 열차번호)
	private String btrainNo;

	// 종착 지하철역ID
	private String bstatnId;

	// 종착 지하철역명
	private String bstatnNm;

	// 열차도착정보를 생성한 시각
	private String recptnDt;

	// 첫번째도착메세지 (도착, 출발 , 진입 등)
	private String arvlMsg2;

	// 두번째도착메세지 (종합운동장 도착, 12분 후 (광명사거리) 등)
	private String arvlMsg3;

	/**
	 * 도착코드
	 * (0:진입, 1:도착, 2:출발, 3:전역출발, 4:전역진입, 5:전역도착, 99:운행중)
	 */
	private String arvlCd;

	// 막차 여부 (1:막차, 0:아님)
	private String lstcarAt;
}
