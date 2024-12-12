package com.swyp.mema.domain.voteLocation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateLocationReq {

	@NotBlank
	private String stationId;

	@NotBlank
	private String stationName;

	@NotBlank
	private String routeName;
}