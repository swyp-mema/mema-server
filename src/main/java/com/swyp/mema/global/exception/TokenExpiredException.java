package com.swyp.mema.global.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class TokenExpiredException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.INVALID_AUTH_TOKEN;

    public TokenExpiredException() {
        super(ERROR_CODE);
    }
}