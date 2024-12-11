package com.swyp.mema.global.base.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

	// User
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "존재하지 않는 유저입니다."),
	USER_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "U002", "사용자가 이미 등록된 약속원입니다."),

	// Member
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 회원입니다."),
	EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "M002", "이미 가입된 이메일입니다."),

	// MEET
	MEET_NOT_FOUND(HttpStatus.NOT_FOUND, "ME001", "존재하지 않는 약속입니다."),
	INVALID_JOIN_CODE(HttpStatus.BAD_REQUEST, "ME002", "유효하지 않은 참여 코드입니다."),

	// MEET MEMBER
	MEET_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEM001", "존재하지 않는 약속원입니다."),
	NOT_MEET_MEMBER(HttpStatus.NOT_FOUND, "MEM002", "해당 약속의 약속원이 아닙니다."),

	// DATE VOTE
	VOTE_DATE_NOT_FOUND(HttpStatus.NOT_FOUND, "VD001", "해당 약속원 ID로 날짜 투표를 찾을 수 없습니다."),
	EXPIRED_VOTE_DATE(HttpStatus.BAD_REQUEST, "VD002", "해당 투표는 이미 만료되었습니다."),
	FAST_EXPIRATION_DATE(HttpStatus.BAD_REQUEST, "VD003", "만료일이 현재 시각보다 이전입니다."),

	// LOCATION VOTE
	VOTE_LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "VL001", "존재하지 않는 위치 투표 ID 입니다."),


	// 400 Bad Request
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "입력 값이 올바르지 않습니다."),
	MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C002", "필수 입력 값이 누락되었습니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C003", "허용되지 않은 HTTP 메서드입니다."),
	INVALID_REQUEST_FORMAT(HttpStatus.BAD_REQUEST, "C004", "요청 형식이 올바르지 않습니다."),
	VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "C005", "유효성 검증에 실패했습니다."),

	// 401 Unauthorized
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증이 필요합니다."),
	INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "유효하지 않은 인증 토큰입니다."),
	ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "A003", "접근 권한이 없습니다."),

	// 403 Forbidden
	FORBIDDEN(HttpStatus.FORBIDDEN, "F001", "접근이 금지되었습니다."),

	// 404 Not Found
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "요청한 리소스를 찾을 수 없습니다."),
	API_NOT_FOUND(HttpStatus.NOT_FOUND, "N002", "요청한 API를 찾을 수 없습니다."),

	// 409 Conflict
	DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "D001", "이미 존재하는 리소스입니다."),
	CONFLICT_STATE(HttpStatus.CONFLICT, "D002", "요청이 현재 리소스 상태와 충돌합니다."),

	// 500 Internal Server Error
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 내부 오류가 발생했습니다."),
	DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S002", "데이터베이스 처리 중 오류가 발생했습니다."),
	UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S003", "알 수 없는 서버 오류가 발생했습니다.");


	private final HttpStatus status;
	private final String code;
	private final String message;

	ErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
