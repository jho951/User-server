package com.userservice.app.domain.user.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userservice.app.domain.user.constant.UserSocialType;
import com.userservice.app.domain.user.entity.UserSocial;

/** 사용자 소셜 계정 저장소입니다. */
public interface UserSocialRepository extends JpaRepository<UserSocial, UUID> {

	/**
	 * 소셜 제공자와 제공자 식별값의 중복 여부를 확인합니다.
	 * @param socialType 소셜 제공자 타입
	 * @param providerId 제공자 사용자 식별값
	 * @return 중복 여부
	 */
	boolean existsBySocialTypeAndProviderId(UserSocialType socialType, String providerId);

	/**
	 * 소셜 제공자와 제공자 식별값으로 소셜 계정 연동 정보를 조회합니다.
	 * @param socialType 소셜 제공자 타입
	 * @param providerId 제공자 사용자 식별값
	 * @return 소셜 계정 조회 결과
	 */
	Optional<UserSocial> findBySocialTypeAndProviderId(UserSocialType socialType, String providerId);
}
