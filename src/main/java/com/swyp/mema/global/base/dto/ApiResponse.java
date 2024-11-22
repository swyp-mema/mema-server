package com.swyp.mema.global.base.dto;

public record ApiResponse<T> (
	T data
){

}
