package com.userservice.app.domain.user.converter;

import com.userservice.app.domain.user.constant.UserSocialType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * {@link UserSocialType}과 DB 코드 문자열을 상호 변환합니다.
 */
@Converter(autoApply = false)
public class UserSocialTypeConverter implements AttributeConverter<UserSocialType, String> {

	@Override
	/**
	 * enum 값을 DB 저장 문자열로 변환합니다.
	 *
	 * @param attribute 소셜 제공자 타입
	 * @return DB 저장 코드
	 */
	public String convertToDatabaseColumn(UserSocialType attribute) {
		return attribute == null ? null : attribute.getCode();
	}

	@Override
	/**
	 * DB 저장 문자열을 enum으로 변환합니다.
	 *
	 * @param dbData DB 저장 코드
	 * @return 소셜 제공자 타입
	 */
	public UserSocialType convertToEntityAttribute(String dbData) {
		return dbData == null ? null : UserSocialType.fromCode(dbData);
	}
}
