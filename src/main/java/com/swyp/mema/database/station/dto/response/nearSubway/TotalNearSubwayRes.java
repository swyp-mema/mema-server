package com.swyp.mema.database.station.dto.response.nearSubway;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TotalNearSubwayRes {
	List<NearSubwayRes> subwayTimeList;
}
