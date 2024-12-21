package com.swyp.mema.domain.user.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class EmailAuthCodeFail extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.EMAIL_AUTH_CODE_FAIL;

    public EmailAuthCodeFail() {
        super(ERROR_CODE);
    }
}
