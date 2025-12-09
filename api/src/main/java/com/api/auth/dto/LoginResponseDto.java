package com.api.auth.dto;

import com.core.constant.UserRole;

public class LoginResponseDto {

	private Long id;
	private String username;
	private String email;
	private UserRole role;

	// 나중에 JWT 붙일 거면 여기 accessToken 같은 필드 추가하면 됨
	// private String accessToken;

	public LoginResponseDto(Long id, String username, String email, UserRole role) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public UserRole getRole() {
		return role;
	}
}
