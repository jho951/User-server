package com.core.exception;

/** 서비스 전반에서 사용하는 공통 비즈니스 예외 */
public class BusinessException extends RuntimeException {

	private final ErrorCode errorCode;

	/**
	 * 기본 메시지로 예외를 생성합니다.
	 *
	 * @param errorCode 에러 코드
	 */
	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	/**
	 * 커스텀 메시지로 예외를 생성합니다.
	 *
	 * @param errorCode 에러 코드
	 * @param message 사용자 정의 메시지
	 */
	public BusinessException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
	public int getHttpStatus() {return errorCode.getHttpStatus();}
	public int getCode() {
		return errorCode.getCode();
	}
}
