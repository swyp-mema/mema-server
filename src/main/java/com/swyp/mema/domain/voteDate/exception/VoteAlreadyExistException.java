package com.swyp.mema.domain.voteDate.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class VoteAlreadyExistException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.VOTE_ALREADY_EXIST;

	public VoteAlreadyExistException() {
		super(ERROR_CODE);
	}
}
