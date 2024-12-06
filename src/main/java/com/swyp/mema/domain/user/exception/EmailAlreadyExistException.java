package com.swyp.mema.domain.user.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class EmailAlreadyExistException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.EMAIL_ALREADY_EXIST;

    public EmailAlreadyExistException() {
        super(ERROR_CODE);
    }
}
