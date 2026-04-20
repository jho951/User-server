package com.userservice.app.domain.user.dto;

import java.util.List;
import java.util.UUID;

import com.userservice.common.base.dto.BaseResponse;
import com.userservice.app.domain.user.constant.UserSocialType;
import com.userservice.app.domain.user.constant.UserStatus;
import com.userservice.app.domain.user.entity.User;
import com.userservice.app.domain.user.entity.UserSocial;
import com.userservice.app.domain.user.constant.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 사용자 API 응답 DTO를 모아둔 클래스입니다.
 */
public class UserResponse {

	/**
	 * 사용자 소셜 계정 응답 DTO입니다.
	 */
	@Getter
	@SuperBuilder
	public static class UserSocialResponse extends BaseResponse {
		private UUID id;
		private UUID userId;
		private UserSocialType provider;
		private String providerUserId;
		private String email;
		private UserSocialType socialType;
		private String providerId;

		/**
		 * 엔티티를 응답 DTO로 변환합니다.
		 *
		 * @param userSocial 사용자 소셜 계정 엔티티
		 * @return 소셜 계정 응답 DTO
		 */
		public static UserSocialResponse from(UserSocial userSocial) {
			return UserSocialResponse.builder()
				.id(userSocial.getId())
				.version(userSocial.getVersion())
				.createdAt(userSocial.getCreatedAt())
				.modifiedAt(userSocial.getModifiedAt())
				.userId(userSocial.getUser().getId())
				.provider(userSocial.getSocialType())
				.providerUserId(userSocial.getProviderId())
				.email(userSocial.getEmail())
				.socialType(userSocial.getSocialType())
				.providerId(userSocial.getProviderId())
				.build();
		}
	}

	/**
	 * 사용자 상세 응답 DTO입니다.
	 */
	@Getter
	@SuperBuilder
	public static class UserDetailResponse extends BaseResponse {
		private UUID id;
		private String email;
		private UserRole role;
		private UserStatus status;
		private List<UserSocialResponse> userSocialList;

		/**
		 * 엔티티를 상세 응답 DTO로 변환합니다.
		 *
		 * @param user 사용자 엔티티
		 * @return 사용자 상세 응답 DTO
		 */
		public static UserDetailResponse from(User user) {
			return UserDetailResponse.builder()
				.id(user.getId())
				.version(user.getVersion())
				.email(user.getEmail())
				.role(user.getRole())
				.status(user.getStatus())
				.createdAt(user.getCreatedAt())
				.modifiedAt(user.getModifiedAt())
				.userSocialList(user.getUserSocialList().stream()
					.map(UserSocialResponse::from)
					.toList())
				.build();
		}
	}

	/**
	 * 사용자 생성 응답 DTO입니다.
	 */
	@Getter
	@Builder
	public static class UserCreateResponse {
		private UserDetailResponse user;

		/**
		 * 엔티티를 생성 응답 DTO로 변환합니다.
		 *
		 * @param user 사용자 엔티티
		 * @return 사용자 생성 응답 DTO
		 */
		public static UserCreateResponse from(User user) {
			return UserCreateResponse.builder()
				.user(UserDetailResponse.from(user))
				.build();
		}
	}
}
