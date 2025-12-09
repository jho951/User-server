package com.api.user.service;

import com.api.user.domain.User;
import com.api.user.dto.UserRequestDto;
import com.api.user.repository.UserRepository;
import com.core.constant.UserRole;
import com.core.exception.BusinessException;
import com.core.exception.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService implements UserServiceInterface {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(
		UserRepository userRepository,
		PasswordEncoder passwordEncoder
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public User findByIdOrThrow(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
	}

	@Override
	public User create(UserRequestDto request) {
		// 이메일 중복 체크
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}

		String encodedPassword = passwordEncoder.encode(request.getPassword());

		User user = User.builder()
			.username(request.getUsername())
			.password(encodedPassword)
			.email(request.getEmail())
			.role(request.getRole() != null ? request.getRole() : UserRole.ADMIN)
			.enabled(request.getEnabled() != null ? request.getEnabled() : Boolean.TRUE)
			.build();

		return userRepository.save(user);
	}

	@Override
	public User update(Long id, UserRequestDto request) {
		User user = findByIdOrThrow(id);

		// 필요한 필드만 선택적으로 업데이트
		if (request.getEmail() != null) {
			user.changeEmail(request.getEmail());
		}

		if (request.getRole() != null) {
			user.changeRole(request.getRole());
		}

		if (request.getEnabled() != null) {
			// 스타일 1) 단순히 Boolean 값 그대로 반영
			user.changeEnabled(request.getEnabled());
		}

		if (request.getPassword() != null && !request.getPassword().isBlank()) {
			String encodedPassword = passwordEncoder.encode(request.getPassword());
			user.changePassword(encodedPassword);
		}

		return user;
	}


	@Override
	public void delete(Long id) {
		User user = findByIdOrThrow(id);
		userRepository.delete(user);
	}
}
