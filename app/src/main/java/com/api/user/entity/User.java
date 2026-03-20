package com.api.user.entity;

import java.util.List;
import java.util.ArrayList;

import com.api.user.constant.UserStatus;
import com.api.user.converter.UserStatusConverter;

import com.core.constant.UserRole;
import com.core.entity.BaseEntity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Enumerated;
import jakarta.persistence.CascadeType;

import lombok.Getter;
import lombok.Builder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 회원을 관리하는 엔티티입니다.
 * <p>1:N 양방향 관계로 {@code UserSocial}에서 관계를 관리합니다.</p>
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "user_id", nullable = false))
public class User extends BaseEntity {

	@Column(nullable = false, unique = true, length = 191)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private UserRole role;

	@Convert(converter = UserStatusConverter.class)
	@Column(nullable = false, length = 1)
	private UserStatus status;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<UserSocial> userSocialList = new ArrayList<>();


	/**
	 * 유저 엔티티를 생성하는 빌더입니다.
	 * @param email    로그인 이메일
	 * @param role     사용자 역할
	 * @param status   사용자 상태 (예: A,P,D,S)
	 */
	@Builder
	private User(String email, UserRole role, UserStatus status) {
		this.email = email;
		this.role = role;
		this.status = status;
	}

	public void changeStatus(UserStatus status) {
		this.status = status;
	}
	public void addUserSocial(UserSocial userSocial) {
		userSocialList.add(userSocial);
	}
}
