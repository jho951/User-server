package com.api.user.controller;

import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.code.SuccessCode;
import com.api.common.dto.GlobalResponse;
import com.api.user.dto.UserRequest;
import com.api.user.dto.UserResponse;
import com.api.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@ConditionalOnProperty(prefix = "features.public-user-api", name = "enabled", havingValue = "true")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PreAuthorize("@jwtAccessPolicy.hasActiveStatus(authentication)")
	@GetMapping("/me")
	public ResponseEntity<GlobalResponse<UserResponse.UserDetailResponse>> me(@AuthenticationPrincipal Jwt jwt) {
		UUID userId = UUID.fromString(jwt.getSubject());
		SuccessCode successCode = SuccessCode.GET_SUCCESS;
		return ResponseEntity
			.status(successCode.getHttpStatus())
			.body(GlobalResponse.ok(successCode, userService.get(userId)));
	}

	@PostMapping("/signup")
	public ResponseEntity<GlobalResponse<UserResponse.UserCreateResponse>> signup(
		@Valid @RequestBody UserRequest.UserSignupRequest request
	) {
		SuccessCode successCode = SuccessCode.CREATE_SUCCESS;
		return ResponseEntity
			.status(successCode.getHttpStatus())
			.body(GlobalResponse.ok(successCode, userService.signup(request)));
	}
}
