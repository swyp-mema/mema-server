package com.swyp.mema.global.base.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

	private final ErrorCode errorCode; // 커스텀 에러 코드

	public ServiceException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

}