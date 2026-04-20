package com.userservice.app.domain.user.constant;

/**
 * 소셜 로그인 제공자 타입을 정의합니다.
 */
public enum UserSocialType {
    /** 일반 */
	DEFAULT("DEF", "일반 로그인"),
	/** 구글 */
	GOOGLE("GGL", "구글"),
	/** 카카오 */
	KAKAO("KKO", "카카오"),
	/** 깃허브 */
	GITHUB("GHB", "깃허브");

	private final String code;
	private final String description;

	/**
	 * 생성자
	 *
	 * @param code DB 저장 코드
	 * @param description 제공자 설명
	 */
	UserSocialType(String code, String description) {
		this.code = code;
		this.description = description;
	}

	/**
	 * DB 저장 코드를 반환합니다.
	 *
	 * @return 소셜 제공자 코드
	 */
	public String getCode() { return code; }

	/**
	 * 소셜 제공자 설명을 반환합니다.
	 *
	 * @return 소셜 제공자 설명
	 */
	public String getDescription() { return description; }

	/**
	 * DB에서 읽은 코드를 enum으로 변환합니다.
	 *
	 * @param dbData DB 저장 코드
	 * @return 매핑된 소셜 제공자 타입
	 */
	public static UserSocialType fromCode(String dbData) {
		if (dbData == null) return null;

		for (UserSocialType type : values()) {
			if (type.code.equals(dbData.trim())) {
				return type;
			}
		}

		throw new IllegalArgumentException("Unknown user social type code: " + dbData);
	}
}
