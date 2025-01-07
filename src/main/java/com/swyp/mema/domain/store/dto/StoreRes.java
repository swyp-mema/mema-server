package com.swyp.mema.domain.store.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreRes {

	private String name;

	private String link;

	private String category;

	private String address;

	private String mapx;

	private String mapy;

	private ImageRes imageInfo;

	@Builder
	public StoreRes(String name, String link, String category, String address, String mapx, String mapy) {
		this.name = name;
		this.link = link;
		this.category = category;
		this.address = address;
		this.mapx = mapx;
		this.mapy = mapy;
	}

	public void setImageInfo(ImageRes imageInfo) {
		this.imageInfo = imageInfo;
	}
}
