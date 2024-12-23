package com.swyp.mema.domain.store.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreRes {

	private String name;

	private String description;

	private String category;

	private String address;

	private String time;

	private String phone;

	private String score;

}
