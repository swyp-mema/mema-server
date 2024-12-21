package com.swyp.mema.domain.voteDate.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class InvalidFinalVoteDateException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.INVALID_FINAL_DATE;

	public InvalidFinalVoteDateException() {
		super(ERROR_CODE);
	}
}
