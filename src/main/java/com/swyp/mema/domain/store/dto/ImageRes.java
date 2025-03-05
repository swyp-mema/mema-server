package com.swyp.mema.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageRes {

	private String url; // Base64 encoded image
	private int width;    // Target width
	private int height;   // Target height

}
