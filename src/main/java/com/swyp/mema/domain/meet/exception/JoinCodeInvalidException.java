package com.swyp.mema.domain.meet.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class JoinCodeInvalidException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.INVALID_JOIN_CODE;

	public JoinCodeInvalidException() {
		super(ERROR_CODE);
	}
}
