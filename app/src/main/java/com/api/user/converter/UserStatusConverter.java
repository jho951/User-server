package com.api.user.converter;

import com.api.user.constant.UserStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {

	@Override
	public String convertToDatabaseColumn(UserStatus attribute) {
		if(attribute == null) return null;
		return attribute.getCode();
	}

	@Override
	public UserStatus convertToEntityAttribute(String dbData) {
		if(dbData == null) return null;
		return UserStatus.fromCode(dbData);
	}
}
