package com.swyp.mema.domain.meet.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class MaxActiveMeetsExceededException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.MAX_ACTIVE_MEET_COUNT;

	public MaxActiveMeetsExceededException() {
		super(ERROR_CODE);
	}

}
