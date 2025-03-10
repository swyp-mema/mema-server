package com.swyp.mema.database.openapi.location.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.swyp.mema.domain.station.dto.response.nearSubway.NearSubwayBasicRes;
import com.swyp.mema.domain.station.dto.response.nearSubway.NearSubwayRes;
import com.swyp.mema.domain.station.dto.response.nearSubway.TotalNearSubwayRes;
import com.swyp.mema.domain.station.dto.response.subwayInfo.SingleStationRes;
import com.swyp.mema.database.openapi.location.response.SubwayMasterBasicResponse;
import com.swyp.mema.database.openapi.location.response.SubwayMasterResponse;
import com.swyp.mema.database.openapi.location.response.TotalSubwayMasterResponse;
import com.swyp.mema.domain.station.dto.response.subwayTime.SubwayTimeBasicRes;
import com.swyp.mema.domain.station.dto.response.subwayTime.TotalSubwayTimeRes;
import com.swyp.mema.domain.station.dto.response.subwayTime.SubwayTimeRes;
import com.swyp.mema.domain.station.dto.response.subwayInfo.TotalStationRes;
import com.swyp.mema.domain.station.model.Station;

@Component
public class StationMasterConverter {

	private static final int START_INDEX = 1;
	private static final int END_INDEX = 1000;

	public TotalStationRes toTotalStationResponse(List<Station> stationList) {

		List<SingleStationRes> stationResponse = stationList.stream()
			.map(station -> SingleStationRes.builder()
				.stationName(station.getStationName())
				.lineName(station.getLineName())
				.lat(station.getLat())
				.lot(station.getLot())
				.build()
			)
			.collect(Collectors.toList());

		int totalCount = stationResponse.size();
		return TotalStationRes.builder()
			.pageNo(START_INDEX)
			.numOfRows(END_INDEX)
			.totalCount(totalCount)
			.stationList(stationResponse)
			.build();
	}

	public TotalSubwayTimeRes toSubwayTimeListResponse(SubwayTimeBasicRes basicResponse) {

		List<SubwayTimeRes> list = basicResponse.getResponse().getBody().getItems().getItemList().stream()
			.map(res -> SubwayTimeRes.builder()
				.stationId(res.getStationId())
				.stationName(res.getStationName())
				.routeId(res.getRouteId())
				.dailyTypeCode(res.getDailyTypeCode())
				.departureTime(res.getDepartureTime())
				.arrivalTime(res.getArrivalTime())
				.endStationId(res.getEndStationId())
				.endStationName(res.getEndStationName())
				.upDownTypeCode(res.getUpDownTypeCode())
				.build()
			).toList();

		return new TotalSubwayTimeRes(list);
	}

	public TotalNearSubwayRes toNearSubwayBasicResponse(NearSubwayBasicRes basicResponse) {

		// Stream을 통해 realtimeArrivalList를 NearSubwayResponse로 변환
		List<NearSubwayRes> responses = basicResponse.getRealtimeArrivalList().stream()
			.map(res -> NearSubwayRes.builder()
				.rowNum(res.getRowNum())                        // 순서
				.subwayLine(res.getSubwayId())          // 지하철호선ID
				.statnFid(res.getStatnFid())                    // 이전 지하철역 ID
				.statnTid(res.getStatnTid())                    // 다음 지하철역 ID
				.statnId(res.getStatnId())                      // 지하철역 ID
				.statnNm(res.getStatnNm())                      // 지하철역명
				.trnsitCo(res.getTrnsitCo())                    // 환승노선수
				.ordkey(res.getOrdkey())                        // 도착예정열차순번
				.subwayList(res.getSubwayList())                // 연계호선ID
				.statnList(res.getStatnList())                  // 연계지하철역ID
				.btrainSttus(res.getBtrainSttus())              // 열차종류
				.barvlDt(res.getBarvlDt())                      // 열차도착 예정시간
				.btrainNo(res.getBtrainNo())                    // 열차번호
				.bstatnId(res.getBstatnId())                    // 종착 지하철역ID
				.bstatnNm(res.getBstatnNm())                    // 종착 지하철역명
				.recptnDt(res.getRecptnDt())                    // 열차도착정보 생성 시각
				.arvlMsg2(res.getArvlMsg2())                    // 첫번째 도착메시지
				.arvlMsg3(res.getArvlMsg3())                    // 두번째 도착메시지
				.arvlCd(res.getArvlCd())                        // 도착코드
				.lstcarAt(res.getLstcarAt())                    // 막차 여부
				.build()
			)
			.toList(); // 결과를 리스트로 변환

		// TotalNearSubwayResponse에 변환된 리스트를 설정
		return new TotalNearSubwayRes(responses);
	}

	public List<SubwayMasterResponse> toSubwayMasterListResponse(SubwayMasterBasicResponse basicResponse) {

		return basicResponse.getSubwayStationMaster().getRows().stream()
			.map(res -> SubwayMasterResponse.builder()
				.stationName(res.getBuildingName())
				.line(res.getRoute())
				.lat(res.getLatitude())
				.lot(res.getLongitude())
				.build()
			).toList();

	}

	public TotalSubwayMasterResponse toTotalSubwayMasterResponse(List<SubwayMasterResponse> responses) {
		return new TotalSubwayMasterResponse(responses);
	}
}
