package com.api.user.domain;

import com.core.constant.UserRole;
import com.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Column(nullable = false, unique = true, length = 50)
	private String username;

	@Column(nullable = false, length = 100)
	private String password;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private UserRole role;

	@Column(nullable = false)
	private Boolean enabled;

	@Builder
	private User(
		String username,
		String password,
		String email,
		UserRole role,
		Boolean enabled
	) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.enabled = enabled;
	}

	// --- 도메인 메서드들 ---

	public void changePassword(String encodedPassword) {
		this.password = encodedPassword;
	}

	public void changeRole(UserRole role) {
		this.role = role;
	}

	public void changeEmail(String email) {
		this.email = email;
	}

	public void changeEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void enable() {
		this.enabled = true;
	}

	public void disable() {
		this.enabled = false;
	}
}
