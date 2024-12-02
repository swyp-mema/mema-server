package com.swyp.mema.domain.meet.exception;

import com.swyp.mema.global.base.exception.ErrorCode;

public class MeetNotFoundException extends RuntimeException {

	private static final ErrorCode ERROR_CODE = ErrorCode.MEET_NOT_FOUNT;

	public MeetNotFoundException() {
		super(ERROR_CODE.getMessage());
	}
}