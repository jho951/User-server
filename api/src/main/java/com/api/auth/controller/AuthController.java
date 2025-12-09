package com.api.auth.controller;

import com.api.auth.dto.LoginRequestDto;
import com.api.auth.dto.LoginResponseDto;
import com.api.user.domain.User;
import com.api.user.repository.UserRepository;
import com.core.dto.BaseResponse;
import com.core.exception.BusinessException;
import com.core.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthController(
		UserRepository userRepository,
		PasswordEncoder passwordEncoder
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/login")
	public ResponseEntity<BaseResponse<LoginResponseDto>> login(
		@RequestBody LoginRequestDto request
	) {
		// 1) 이메일로 사용자 조회
		User user = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		// 2) 비밀번호 체크
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new BusinessException(ErrorCode.INVALID_LOGIN);
		}

		// 3) 응답 DTO 생성 (JWT는 v2에서)
		LoginResponseDto body = new LoginResponseDto(
			user.getId(),
			user.getUsername(),
			user.getEmail(),
			user.getRole()
		);

		return ResponseEntity.ok(BaseResponse.ok(body, "로그인 성공"));
	}
}
