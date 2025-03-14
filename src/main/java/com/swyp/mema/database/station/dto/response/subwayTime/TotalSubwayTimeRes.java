package com.swyp.mema.database.station.dto.response.subwayTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TotalSubwayTimeRes {
	List<SubwayTimeRes> subwayTimeList;
}
