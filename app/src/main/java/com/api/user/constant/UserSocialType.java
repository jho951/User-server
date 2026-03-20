package com.api.user.constant;

/** 소셜 로그인 제공자 */
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

	public String getCode() { return code; }
	public String getDescription() { return description; }

	/** DB에서 읽어온 짧은 코드를 Enum 객체로 변환하는 유틸리티 메서드 */
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
