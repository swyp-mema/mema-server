package com.swyp.mema.domain.voteLocation.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class DuplicateVoteException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.DUPLICATE_LOCATION_VOTE;

	public DuplicateVoteException() {
		super(ERROR_CODE);
	}

}
