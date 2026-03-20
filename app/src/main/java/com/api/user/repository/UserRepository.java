package com.api.user.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.user.entity.User;

/**
 * 사용자 엔티티 저장소입니다.
 */
public interface UserRepository extends JpaRepository<User, UUID> {

	/**
	 * 이메일 중복 여부를 확인합니다.
	 *
	 * @param email 사용자 이메일
	 * @return 중복 여부
	 */
	boolean existsByEmail(String email);

	/**
	 * 소셜 계정 목록을 함께 로딩하여 사용자를 조회합니다.
	 *
	 * @param id 사용자 식별자
	 * @return 사용자 조회 결과
	 */
	@EntityGraph(attributePaths = "userSocialList")
	Optional<User> findWithUserSocialListById(UUID id);

	/**
	 * 소셜 계정 목록을 함께 로딩하여 이메일로 사용자를 조회합니다.
	 *
	 * @param email 사용자 이메일
	 * @return 사용자 조회 결과
	 */
	@EntityGraph(attributePaths = "userSocialList")
	Optional<User> findWithUserSocialListByEmail(String email);
}
