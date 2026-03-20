package com.core.exception;

/** 공통 에러 코드 정의 */
public enum ErrorCode {

	// 공통 에러
	BAD_REQUEST(400, 8000, "잘못된 요청입니다."),
	UNAUTHORIZED(401, 8001, "인증이 필요합니다."),
	FORBIDDEN(403, 8002, "접근 권한이 없습니다."),
	NOT_FOUND(404, 8003, "리소스를 찾을 수 없습니다."),
	INTERNAL_SERVER_ERROR(500, 8004, "서버 오류가 발생했습니다."),

	// 사용자 관련 에러 예시
	USER_NOT_FOUND(404, 8100, "사용자를 찾을 수 없습니다."),
	EMAIL_ALREADY_EXISTS(400, 8101, "이미 사용 중인 이메일입니다."),
	SOCIAL_ACCOUNT_ALREADY_EXISTS(400, 8102, "이미 연결된 소셜 계정입니다."),
	INVALID_LOGIN(400, 8103, "이메일 또는 비밀번호가 올바르지 않습니다."),
	USER_DISABLED(400, 8104, "허용되지 않는 유저입니다."),
	INVALID_CREDENTIALS(400, 8105, "권한이 없습니다."),

	// 토큰 에러
	NOT_FOUND_SECRET(400, 8200, "비밀키가 비어있습니다."),
	INVALID_SECRET(401, 8201, "최소 32바이트 이상의 문자열이여야합니다."),
	INVALID_TOKEN(400, 8202, "유효하지 않은 리프레시 토큰입니다.");

	private final int httpStatus;
	private final int code;
	private final String message;

	/**
	 * 생성자
	 *
	 * @param httpStatus HTTP status code 숫자
	 * @param code 비즈니스 에러 코드
	 * @param message 기본 에러 메시지
	 */
	ErrorCode(int httpStatus, int code, String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}

	public int getHttpStatus() {return httpStatus;}
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}
