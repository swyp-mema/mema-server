package com.swyp.mema.global.validation;

public interface EntityValidator<T, ID> {

    T validateExists(ID id);
}
