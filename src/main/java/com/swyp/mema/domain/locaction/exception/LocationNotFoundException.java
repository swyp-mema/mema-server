package com.swyp.mema.domain.locaction.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class LocationNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.VOTE_LOCATION_NOT_FOUND;

	public LocationNotFoundException() {
		super(ERROR_CODE);
	}
}
