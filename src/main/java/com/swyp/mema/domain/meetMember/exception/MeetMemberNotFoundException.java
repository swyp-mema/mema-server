package com.swyp.mema.domain.meetMember.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class MeetMemberNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.MEET_MEMBER_NOT_FOUND;

	public MeetMemberNotFoundException() {
		super(ERROR_CODE);
	}
}
