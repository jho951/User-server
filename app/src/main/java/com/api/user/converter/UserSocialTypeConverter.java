package com.api.user.converter;

import com.api.user.constant.UserSocialType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class UserSocialTypeConverter implements AttributeConverter<UserSocialType, String> {

	@Override
	public String convertToDatabaseColumn(UserSocialType attribute) {
		return attribute == null ? null : attribute.getCode();
	}

	@Override
	public UserSocialType convertToEntityAttribute(String dbData) {
		return dbData == null ? null : UserSocialType.fromCode(dbData);
	}
}
