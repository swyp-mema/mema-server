package com.swyp.mema.domain.store.exception;

import com.swyp.mema.global.base.exception.ErrorCode;
import com.swyp.mema.global.base.exception.ServiceException;

public class NotRecommendStore extends ServiceException {

	private static final ErrorCode ERROR_CODE = ErrorCode.NOT_RECOMMEND_STORE;

	public NotRecommendStore() {
		super(ERROR_CODE);
	}

}
