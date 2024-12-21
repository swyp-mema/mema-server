package com.swyp.mema.domain.station.dto.response.subwayTime;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data

public class SubwayTimeBasicResponse {

	@JsonProperty("response")
	private Response response;

	@Data
	public static class Response {

		@JsonProperty("header")
		private Header header;

		@JsonProperty("body")
		private Body body;
	}

	@Data
	public static class Header {

		@JsonProperty("resultCode")
		private String resultCode;

		@JsonProperty("resultMsg")
		private String resultMsg;
	}

	@Data
	public static class Body {

		@JsonProperty("items")
		private Items items = new Items(); // 기본값으로 빈 객체 초기화

		@JsonProperty("pageNo")
		private int pageNo;
		@JsonProperty("numOfRows")
		private int numOfRows;
		@JsonProperty("totalCount")
		private int totalCount;
	}

	@Data
	public static class Items {

		@JsonProperty("item")
		private List<Item> itemList = new ArrayList<>(); // 기본값으로 빈 리스트 초기화;
	}

	@Data
	public static class Item {

		@JsonProperty("arrTime")
		private String arrivalTime;

		@JsonProperty("dailyTypeCode")
		private String dailyTypeCode;

		@JsonProperty("depTime")
		private String departureTime;

		@JsonProperty("endSubwayStationId")
		private String endStationId;

		@JsonProperty("endSubwayStationNm")
		private String endStationName;

		@JsonProperty("subwayRouteId")
		private String routeId;

		@JsonProperty("subwayStationId")
		private String stationId;

		@JsonProperty("subwayStationNm")
		private String stationName;

		@JsonProperty("upDownTypeCode")
		private String upDownTypeCode;
	}
}
