package com.swyp.mema.domain.voteLocation.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class OnlyOneVoteException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.ONLY_ONE_LOCATION_VOTE;

	public OnlyOneVoteException() {
		super(ERROR_CODE);
	}
}
