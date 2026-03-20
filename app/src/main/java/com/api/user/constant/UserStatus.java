package com.api.user.constant;

import java.util.Arrays;

/** 유저 상태 */
public enum UserStatus {
	ACTIVE("정상", "A"),
	PENDING("승인대기", "P"),
	SUSPENDED("정지", "S"),
	DELETED("탈퇴", "D");

	private final String description;
	private final String code;

	/**
	 * 생성자
	 * @param description 상태 설명
	 * @param code DB에 저장되는 상태 코드
	 */
	UserStatus(String description, String code) {
		this.description = description;
		this.code = code;
	}

	public String getDescription() {
		return description;
	}
	public String getCode() {
		return code;
	}

	/**
	 * enum 내부의 변환 헬퍼
	 * @param code DB에 저장되는 상태 코드
	 * @return Optional<UserStatus> {@code UserStatus.ACTIVE }
	 */
	public static UserStatus fromCode(String code) {
		return Arrays.stream(values())
			.filter(status -> status.code.equals(code))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown user status code: " + code));
	}
}
