package com.swyp.mema.database.station.dto.response.subwayInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SubwayInfoBasicRes {

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
		private Items items;

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
		private List<Item> itemList;
	}

	@Data
	public static class Item {

		@JsonProperty("subwayStationId")
		private String stationId;

		@JsonProperty("subwayStationName")
		private String stationName;

		@JsonProperty("subwayLineName")
		private String lineName;
	}
}
