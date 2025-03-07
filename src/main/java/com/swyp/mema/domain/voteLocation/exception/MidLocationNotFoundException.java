package com.swyp.mema.domain.voteLocation.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class MidLocationNotFoundException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MID_LOCATION_NOT_FOUND;

    public MidLocationNotFoundException() {
        super(ERROR_CODE);
    }
}
