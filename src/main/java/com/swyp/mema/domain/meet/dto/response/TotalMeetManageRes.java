package com.swyp.mema.domain.meet.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TotalMeetManageRes {

	private List<MeetHomeDetailRes> meetList; // 데이터 리스트
	private boolean hasNext;      // 다음 데이터 유무
	private int pageSize;         // 반환된 데이터 크기
}
