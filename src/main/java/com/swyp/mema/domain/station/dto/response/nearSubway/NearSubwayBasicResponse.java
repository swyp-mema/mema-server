package com.swyp.mema.domain.station.dto.response.nearSubway;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NearSubwayBasicResponse {

	@JsonProperty("errorMessage")
	private ErrorMessage errorMessage;

	@JsonProperty("realtimeArrivalList")
	private List<RealtimeArrival> realtimeArrivalList;

	@Data
	public static class ErrorMessage {

		@JsonProperty("status")
		private int status;

		@JsonProperty("code")
		private String code;

		@JsonProperty("message")
		private String message;

		@JsonProperty("link")
		private String link;

		@JsonProperty("developerMessage")
		private String developerMessage;

		@JsonProperty("total")
		private int total;
	}

	@Data
	public static class RealtimeArrival {

		@JsonProperty("beginRow")
		private String beginRow;

		@JsonProperty("endRow")
		private String endRow;

		@JsonProperty("curPage")
		private String curPage;

		@JsonProperty("pageRow")
		private String pageRow;

		@JsonProperty("totalCount")
		private int totalCount;

		@JsonProperty("rowNum")
		private int rowNum;

		@JsonProperty("selectedCount")
		private int selectedCount;

		@JsonProperty("subwayId")
		private String subwayId;

		@JsonProperty("subwayNm")
		private String subwayNm;

		@JsonProperty("updnLine")
		private String updnLine;

		@JsonProperty("trainLineNm")
		private String trainLineNm;

		@JsonProperty("subwayHeading")
		private String subwayHeading;

		@JsonProperty("statnFid")
		private String statnFid;

		@JsonProperty("statnTid")
		private String statnTid;

		@JsonProperty("statnId")
		private String statnId;

		@JsonProperty("statnNm")
		private String statnNm;

		@JsonProperty("trainCo")
		private String trainCo;

		@JsonProperty("trnsitCo")
		private String trnsitCo;

		@JsonProperty("ordkey")
		private String ordkey;

		@JsonProperty("subwayList")
		private String subwayList;

		@JsonProperty("statnList")
		private String statnList;

		@JsonProperty("btrainSttus")
		private String btrainSttus;

		@JsonProperty("barvlDt")
		private String barvlDt;

		@JsonProperty("btrainNo")
		private String btrainNo;

		@JsonProperty("bstatnId")
		private String bstatnId;

		@JsonProperty("bstatnNm")
		private String bstatnNm;

		@JsonProperty("recptnDt")
		private String recptnDt;

		@JsonProperty("arvlMsg2")
		private String arvlMsg2;

		@JsonProperty("arvlMsg3")
		private String arvlMsg3;

		@JsonProperty("arvlCd")
		private String arvlCd;

		@JsonProperty("lstcarAt")
		private String lstcarAt;
	}
}