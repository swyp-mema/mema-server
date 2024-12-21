package com.swyp.mema.domain.station.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class StationNotFoundException extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.STATION_NOT_FOUNT;

	public StationNotFoundException() {
		super(ERROR_CODE);
	}
}
