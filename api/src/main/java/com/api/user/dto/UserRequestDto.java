package com.api.user.dto;

import com.core.constant.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRequestDto {

	private String username;
	private String password;
	private String email;
	private UserRole role;
	private Boolean enabled;
}
