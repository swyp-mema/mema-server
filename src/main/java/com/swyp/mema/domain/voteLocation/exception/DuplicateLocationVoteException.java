package com.swyp.mema.domain.voteLocation.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class DuplicateLocationVoteException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.DUPLICATE_LOCATION_VOTE;

	public DuplicateLocationVoteException() {
		super(ERROR_CODE);
	}

}
