package com.swyp.mema.domain.charge.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class ChargeNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.CHARGE_NOT_FOUND;

	public ChargeNotFoundException() {
		super(ERROR_CODE);
	}
}