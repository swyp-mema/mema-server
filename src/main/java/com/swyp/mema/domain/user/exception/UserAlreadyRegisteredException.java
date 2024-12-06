package com.swyp.mema.domain.user.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class UserAlreadyRegisteredException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.USER_ALREADY_REGISTERED;

	public UserAlreadyRegisteredException() {
		super(ERROR_CODE);
	}
}