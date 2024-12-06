package com.swyp.mema.domain.voteDate.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class VoteDateExpiredException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.EXPIRED_VOTE_DATE;

	public VoteDateExpiredException() {
		super(ERROR_CODE);
	}
}
