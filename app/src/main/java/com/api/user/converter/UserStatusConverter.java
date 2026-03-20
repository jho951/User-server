package com.api.user.converter;

import com.api.user.constant.UserStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * {@link UserStatus}와 DB 코드 문자열을 상호 변환합니다.
 */
@Converter(autoApply = false)
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {

	@Override
	/**
	 * enum 값을 DB 저장 문자열로 변환합니다.
	 *
	 * @param attribute 사용자 상태
	 * @return DB 저장 코드
	 */
	public String convertToDatabaseColumn(UserStatus attribute) {
		if(attribute == null) return null;
		return attribute.getCode();
	}

	@Override
	/**
	 * DB 저장 문자열을 enum으로 변환합니다.
	 *
	 * @param dbData DB 저장 코드
	 * @return 사용자 상태
	 */
	public UserStatus convertToEntityAttribute(String dbData) {
		if(dbData == null) return null;
		return UserStatus.fromCode(dbData);
	}
}
