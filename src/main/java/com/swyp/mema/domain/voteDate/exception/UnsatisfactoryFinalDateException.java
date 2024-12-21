package com.swyp.mema.domain.voteDate.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class UnsatisfactoryFinalDateException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.UNSATISFACTORY_FINAL_DATE;

	public UnsatisfactoryFinalDateException() {
		super(ERROR_CODE);
	}
}
