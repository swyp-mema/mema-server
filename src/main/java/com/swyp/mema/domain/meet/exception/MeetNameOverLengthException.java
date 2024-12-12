package com.swyp.mema.domain.meet.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class MeetNameOverLengthException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.MEET_NAME_OVER_LENGTH;

	public MeetNameOverLengthException() {
		super(ERROR_CODE);
	}
}
