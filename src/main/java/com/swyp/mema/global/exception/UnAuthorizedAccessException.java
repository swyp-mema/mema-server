package com.swyp.mema.global.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class UnAuthorizedAccessException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.UNAUTHORIZED;

    public UnAuthorizedAccessException() {
        super(ERROR_CODE);
    }
}