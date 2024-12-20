package com.swyp.mema.domain.voteDate.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class DuplicateDateVoteException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.DUPLICATE_DATE_VOTE;

	public DuplicateDateVoteException() {
		super(ERROR_CODE);
	}
}

