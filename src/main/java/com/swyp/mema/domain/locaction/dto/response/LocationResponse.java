package com.swyp.mema.domain.locaction.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LocationResponse {

	@JsonProperty("SearchSTNBySubwayLineInfo")
	private SearchSTNBySubwayLineInfo SearchSTNBySubwayLineInfo;

	@Data
	public static class SearchSTNBySubwayLineInfo {

		@JsonProperty("list_total_count")
		private int list_total_count;

		@JsonProperty("RESULT")
		private Result result;

		@JsonProperty("row")
		private List<Row> row;
	}

	@Data
	public static class Result {

		@JsonProperty("CODE")
		private String code;

		@JsonProperty("MESSAGE")
		private String message;

	}

	@Data
	public static class Row {
		@JsonProperty("STATION_CD")
		private String stationCd;     // 역 코드

		@JsonProperty("STATION_NM")
		private String stationNm;     // 역 이름

		@JsonProperty("STATION_NM_ENG")
		private String stationNmEng;  // 역 이름 (영문)

		@JsonProperty("LINE_NUM")
		private String lineNum;       // 호선 번호

		@JsonProperty("FR_CODE")
		private String frCode;        // 외부 코드

		@JsonProperty("STATION_NM_CHN") // JSON의 "STATION_NM_CHN" (한자)
		private String stationNmChn;

		@JsonProperty("STATION_NM_JPN") // JSON의 "STATION_NM_JPN" (일본어)
		private String stationNmJpn;
	}
}
