package com.api.user.controller;

import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.common.code.SuccessCode;
import com.api.common.dto.GlobalResponse;
import com.api.user.constant.UserSocialType;
import com.api.user.dto.UserRequest;
import com.api.user.dto.UserResponse;
import com.api.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/internal/users")
@ConditionalOnProperty(
	prefix = "features.internal-user-api",
	name = "enabled",
	havingValue = "true",
	matchIfMissing = true
)
public class InternalUserController {

	private final UserService userService;

	public InternalUserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<GlobalResponse<UserResponse.UserDetailResponse>> createUser(
		@Valid @RequestBody UserRequest.UserCreateRequest request
	) {
		SuccessCode successCode = SuccessCode.USER_CREATE_SUCCESS;
		return ResponseEntity
			.status(successCode.getHttpStatus())
			.body(GlobalResponse.ok(successCode, userService.create(request)));
	}

	@PostMapping("/social")
	public ResponseEntity<GlobalResponse<UserResponse.UserSocialResponse>> createSocial(
		@Valid @RequestBody UserRequest.UserSocialCreateRequest request
	) {
		SuccessCode successCode = SuccessCode.USER_SOCIAL_CREATE_SUCCESS;
		return ResponseEntity
			.status(successCode.getHttpStatus())
			.body(GlobalResponse.ok(successCode, userService.createSocial(request)));
	}

	@PatchMapping("/{userId}/status")
	public ResponseEntity<GlobalResponse<UserResponse.UserDetailResponse>> updateStatus(
		@PathVariable UUID userId,
		@Valid @RequestBody UserRequest.UserStatusUpdateRequest request
	) {
		SuccessCode successCode = SuccessCode.USER_STATUS_UPDATE_SUCCESS;
		return ResponseEntity
			.status(successCode.getHttpStatus())
			.body(GlobalResponse.ok(successCode, userService.updateStatus(userId, request)));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<GlobalResponse<UserResponse.UserDetailResponse>> getUser(@PathVariable UUID userId) {
		SuccessCode successCode = SuccessCode.USER_GET_SUCCESS;
		return ResponseEntity
			.status(successCode.getHttpStatus())
			.body(GlobalResponse.ok(successCode, userService.get(userId)));
	}

	@GetMapping("/by-email")
	public ResponseEntity<GlobalResponse<UserResponse.UserDetailResponse>> getUserByEmail(@RequestParam String email) {
		SuccessCode successCode = SuccessCode.USER_GET_BY_EMAIL_SUCCESS;
		return ResponseEntity
			.status(successCode.getHttpStatus())
			.body(GlobalResponse.ok(successCode, userService.getByEmail(email)));
	}

	@GetMapping("/by-social")
	public ResponseEntity<GlobalResponse<UserResponse.UserDetailResponse>> getUserBySocial(
		@RequestParam UserSocialType socialType,
		@RequestParam String providerId
	) {
		SuccessCode successCode = SuccessCode.USER_GET_BY_SOCIAL_SUCCESS;
		return ResponseEntity
			.status(successCode.getHttpStatus())
			.body(GlobalResponse.ok(successCode, userService.getBySocial(socialType, providerId)));
	}
}
