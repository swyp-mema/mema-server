package com.swyp.mema.domain.user.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class UserNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.USER_NOT_FOUND;

	public UserNotFoundException() {
		super(ERROR_CODE);
	}
}
