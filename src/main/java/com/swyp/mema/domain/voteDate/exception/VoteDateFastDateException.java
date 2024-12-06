package com.swyp.mema.domain.voteDate.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class VoteDateFastDateException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.FAST_EXPIRATION_DATE;

	public VoteDateFastDateException() {
		super(ERROR_CODE);
	}
}
