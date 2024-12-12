package com.swyp.mema.domain.meet.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class NotEmptyMeetNameException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.MEET_NOT_FOUND;

	public NotEmptyMeetNameException() {
		super(ERROR_CODE);
	}
}
