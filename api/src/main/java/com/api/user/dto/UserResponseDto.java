package com.api.user.dto;

import com.api.user.domain.User;
import com.core.constant.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

	private Long id;
	private String username;
	private String email;
	private UserRole role;
	private Boolean enabled;

	public static UserResponseDto from(User user) {
		return UserResponseDto.builder()
			.id(user.getId())
			.username(user.getUsername())
			.email(user.getEmail())
			.role(user.getRole())
			.enabled(user.getEnabled())
			.build();
	}
}
