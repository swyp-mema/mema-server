package com.swyp.mema.domain.user.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class EmailAuthSessionNotexistException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.EMAIL_AUTH_SESSION_NOT_EXIST;

    public EmailAuthSessionNotexistException() {
        super(ERROR_CODE);
    }
}
