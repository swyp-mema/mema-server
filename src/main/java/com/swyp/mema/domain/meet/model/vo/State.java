package com.swyp.mema.domain.meet.model.vo;

public enum State {
	CREATED,        // 약속 생성
	DATE_VOTING,    // 날짜 투표 중
	LOCATION_VOTING,// 위치 투표 중
	READY,			// 약속 대기
	COMPLETED,      // 약속 완료
	SETTLING        // 정산 중
}
