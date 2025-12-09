package com.api.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.user.domain.User;
import com.core.constant.UserRole;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);

	// ✅ SUPER_ADMIN 유무 체크
	boolean existsByRole(UserRole role);

	Optional<User> findByEmail(String email);
}
