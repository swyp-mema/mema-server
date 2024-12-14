package com.swyp.mema.domain.station.dto.response.subwayMaster;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SubwayMasterBasicResponse {

	@JsonProperty("subwayStationMaster")
	private SubwayStationMaster subwayStationMaster;

	@Data
	public static class SubwayStationMaster {

		@JsonProperty("list_total_count")
		private int listTotalCount;

		@JsonProperty("RESULT")
		private Result result;

		@JsonProperty("row")
		private List<Row> rows;
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

		@JsonProperty("BLDN_ID")
		private String buildingId;

		@JsonProperty("BLDN_NM")
		private String buildingName;

		@JsonProperty("ROUTE")
		private String route;

		@JsonProperty("LAT")
		private String latitude;

		@JsonProperty("LOT")
		private String longitude;
	}
}
