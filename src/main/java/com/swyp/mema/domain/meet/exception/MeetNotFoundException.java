package com.swyp.mema.domain.meet.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;


public class MeetNotFoundException extends ServiceException {
	private static final ErrorCode ERROR_CODE = ErrorCode.MEET_NOT_FOUNT;

	public MeetNotFoundException() {
		super(ERROR_CODE);
	}
}