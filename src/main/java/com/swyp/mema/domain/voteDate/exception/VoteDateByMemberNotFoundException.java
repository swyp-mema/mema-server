package com.swyp.mema.domain.voteDate.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class VoteDateByMemberNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.VOTE_DATE_NOT_FOUND;

	public VoteDateByMemberNotFoundException() {
		super(ERROR_CODE);
	}
}
