package com.api.user.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	boolean existsByEmail(String email);

	@EntityGraph(attributePaths = "userSocialList")
	Optional<User> findWithUserSocialListById(UUID id);

	@EntityGraph(attributePaths = "userSocialList")
	Optional<User> findWithUserSocialListByEmail(String email);
}
