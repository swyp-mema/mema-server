package com.swyp.mema.domain.user.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class EmailNotMineException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.EMAIL_NOT_MINE;

    public EmailNotMineException() {
        super(ERROR_CODE);
    }
}
