package com.swyp.mema.domain.store.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BasicStoreRes {

	@JsonProperty("lastBuildDate")
	private String lastBuildDate;

	@JsonProperty("total")
	private int total;

	@JsonProperty("start")
	private int start;

	@JsonProperty("display")
	private int display;

	@JsonProperty("items")
	private List<Item> items = new ArrayList<>();

	@Data
	public static class Item {
		@JsonProperty("title")
		private String title;

		@JsonProperty("link")
		private String link;

		@JsonProperty("category")
		private String category;

		@JsonProperty("description")
		private String description;

		@JsonProperty("telephone")
		private String telephone;

		@JsonProperty("address")
		private String address;

		@JsonProperty("roadAddress")
		private String roadAddress;

		@JsonProperty("mapx")
		private String mapx;

		@JsonProperty("mapy")
		private String mapy;
	}
}
