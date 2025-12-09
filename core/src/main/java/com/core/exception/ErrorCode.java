package com.core.exception;

/**
 * 공통 에러 코드 정의
 * - status: HTTP status code 숫자 (Spring 의존 X)
 * - code: 비즈니스 에러 코드 문자열
 * - message: 기본 에러 메시지
 */
public enum ErrorCode {

	// 공통 에러
	BAD_REQUEST( "C400",400, "잘못된 요청입니다."),
	UNAUTHORIZED( "C401",401, "인증이 필요합니다."),
	FORBIDDEN( "C403",403, "접근 권한이 없습니다."),
	NOT_FOUND( "C404",404, "리소스를 찾을 수 없습니다."),
	INTERNAL_SERVER_ERROR( "C500",500, "서버 오류가 발생했습니다."),

	// 사용자 관련 에러 예시
	USER_NOT_FOUND("U404", 404, "사용자를 찾을 수 없습니다."),
	EMAIL_ALREADY_EXISTS("U400",400,  "이미 사용 중인 이메일입니다."),
	INVALID_LOGIN( "U401",400, "이메일 또는 비밀번호가 올바르지 않습니다.");



	private final String code;
	private final int status;
	private final String message;

	ErrorCode( String code,int status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
