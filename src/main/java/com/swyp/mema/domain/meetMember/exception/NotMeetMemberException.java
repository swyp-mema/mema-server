package com.swyp.mema.domain.meetMember.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class NotMeetMemberException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.NOT_MEET_MEMBER;

	public NotMeetMemberException() {
		super(ERROR_CODE);
	}
}
