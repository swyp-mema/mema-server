package com.swyp.mema.domain.user.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class PasswordNotChangedException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.PASSWORD_NOT_CHANGED;

    public PasswordNotChangedException() {
        super(ERROR_CODE);
    }
}
