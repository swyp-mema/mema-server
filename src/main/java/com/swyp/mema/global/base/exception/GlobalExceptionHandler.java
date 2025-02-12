package com.swyp.mema.global.base.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.swyp.mema.global.base.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	private void logException(HttpServletRequest request, Exception e) {
		log.error("[EXCEPTION] URI: {}, Method: {}, Type: {}, Message: {}",
			request.getRequestURI(),
			request.getMethod(),
			e.getClass().getSimpleName(),
			e.getMessage()
		);
	}

	/**
	 * 1. 사용자 정의 예외 (ServiceException)
	 */
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ErrorResponse> handleServiceException(HttpServletRequest request, ServiceException e) {
		logException(request, e);
		return ResponseEntity
			.status(e.getErrorCode().getStatus())
			.body(ErrorResponse.of(e.getErrorCode()));
	}

	/**
	 * 2. Bean Validation 예외 (@Valid 사용 시 발생)
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse handleValidationException(HttpServletRequest request, MethodArgumentNotValidException e) {
		logException(request, e);
		return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
	}

	/**
	 * 3. 요청 데이터 부족 예외 (MissingRequestValueException)
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingRequestValueException.class)
	public ErrorResponse handleMissingRequestValueException(HttpServletRequest request, MissingRequestValueException e) {
		logException(request, e);
		return ErrorResponse.of(ErrorCode.MISSING_INPUT_VALUE);
	}

	/**
	 * 4. HTTP 메서드 오류 (예: GET 대신 POST 요청 등)
	 */
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
		logException(request, e);
		return ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
	}

	/**
	 * 5. 리소스를 찾을 수 없는 경우 (NoSuchElementException)
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoSuchElementException.class)
	public ErrorResponse handleNoSuchElementException(HttpServletRequest request, NoSuchElementException e) {
		logException(request, e);
		return ErrorResponse.of(ErrorCode.RESOURCE_NOT_FOUND);
	}

	/**
	 * 6. 존재하지 않는 API 요청 (NoHandlerFoundException)
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoHandlerFoundException.class)
	public ErrorResponse handleNoHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) {
		logException(request, e);
		return ErrorResponse.of(ErrorCode.API_NOT_FOUND);
	}

	/**
	 * 7. 기타 모든 예외 (서버 내부 오류)
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse handleGenericException(HttpServletRequest request, Exception e) {
		logException(request, e);
		return ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
	}
}
