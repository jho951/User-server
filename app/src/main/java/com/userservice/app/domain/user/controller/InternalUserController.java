package com.userservice.app.domain.user.controller;

import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.userservice.common.base.constant.SuccessCode;
import com.userservice.common.base.dto.GlobalResponse;
import com.userservice.app.domain.user.constant.UserSocialType;
import com.userservice.app.domain.user.dto.UserRequest;
import com.userservice.app.domain.user.dto.UserResponse;
import com.userservice.app.domain.user.service.UserService;

import jakarta.validation.Valid;

/**
 * 내부 시스템 간 사용자 연동 API를 제공합니다.
 */
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

	/**
	 * 내부 사용자 API 컨트롤러를 생성합니다.
	 *
	 * @param userService 사용자 서비스
	 */
	public InternalUserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 내부 API를 통해 사용자를 생성합니다.
	 *
	 * @param request 사용자 생성 요청
	 * @return 생성된 사용자 응답
	 */
	@PostMapping
	public ResponseEntity<GlobalResponse<UserResponse.UserDetailResponse>> createUser(
		@Valid @RequestBody UserRequest.UserCreateRequest request
	) {
		return GlobalResponse.success(SuccessCode.USER_CREATE_SUCCESS, userService.create(request));
	}

	/**
	 * 사용자 소셜 계정 연동 정보를 생성합니다.
	 *
	 * @param request 소셜 계정 생성 요청
	 * @return 생성된 소셜 계정 응답
	 */
	@PostMapping("/social")
	public ResponseEntity<GlobalResponse<UserResponse.UserSocialResponse>> createSocial(
		@Valid @RequestBody UserRequest.UserSocialCreateRequest request
	) {
		return GlobalResponse.success(SuccessCode.USER_SOCIAL_CREATE_SUCCESS, userService.createSocial(request));
	}

	/**
	 * 소셜 사용자 보장 유즈케이스를 처리합니다.
	 *
	 * @param request 소셜 사용자 보장 요청
	 * @return 보장된 사용자 상세 응답
	 */
	@PostMapping("/ensure-social")
	public ResponseEntity<GlobalResponse<UserResponse.UserDetailResponse>> ensureSocial(
		@Valid @RequestBody UserRequest.UserEnsureSocialRequest request
	) {
		return GlobalResponse.success(SuccessCode.USER_ENSURE_SOCIAL_SUCCESS, userService.ensureSocial(request));
	}

	/**
	 * 사용자를 조회/생성하고 소셜 계정을 멱등하게 연결합니다.
	 *
	 * @param request 소셜 사용자 보장 요청
	 * @return 보장된 사용자 상세 응답
	 */
	@PostMapping("/find-or-create-and-link-social")
	public ResponseEntity<GlobalResponse<UserResponse.UserDetailResponse>> findOrCreateAndLinkSocial(
		@Valid @RequestBody UserRequest.UserEnsureSocialRequest request
	) {
		return GlobalResponse.success(
			SuccessCode.USER_FIND_OR_CREATE_AND_LINK_SOCIAL_SUCCESS,
			userService.findOrCreateAndLinkSocial(request)
		);
	}

	/**
	 * 사용자 상태를 변경합니다.
	 *
	 * @param userId 사용자 식별자
	 * @param request 상태 변경 요청
	 * @return 변경된 사용자 응답
	 */
	@PutMapping("/{userId}/status")
	public ResponseEntity<GlobalResponse<UserResponse.UserDetailResponse>> updateStatus(
		@PathVariable UUID userId,
		@Valid @RequestBody UserRequest.UserStatusUpdateRequest request
	) {
		return GlobalResponse.success(SuccessCode.USER_STATUS_UPDATE_SUCCESS, userService.updateStatus(userId, request));
	}

	/**
	 * 사용자 식별자로 사용자를 조회합니다.
	 *
	 * @param userId 사용자 식별자
	 * @return 사용자 응답
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<GlobalResponse<UserResponse.UserDetailResponse>> getUser(@PathVariable UUID userId) {
		return GlobalResponse.success(SuccessCode.USER_GET_SUCCESS, userService.get(userId));
	}

	/**
	 * 이메일로 사용자를 조회합니다.
	 *
	 * @param email 사용자 이메일
	 * @return 사용자 응답
	 */
	@GetMapping("/by-email")
	public ResponseEntity<GlobalResponse<UserResponse.UserDetailResponse>> getUserByEmail(@RequestParam String email) {
		return GlobalResponse.success(SuccessCode.USER_GET_BY_EMAIL_SUCCESS, userService.getByEmail(email));
	}

	/**
	 * 소셜 제공자 정보로 사용자를 조회합니다.
	 *
	 * @param socialType 소셜 제공자 타입
	 * @param providerId 제공자 사용자 식별값
	 * @return 사용자 응답
	 */
	@GetMapping("/by-social")
	public ResponseEntity<GlobalResponse<UserResponse.UserDetailResponse>> getUserBySocial(
		@RequestParam UserSocialType socialType,
		@RequestParam String providerId
	) {
		return GlobalResponse.success(
			SuccessCode.USER_GET_BY_SOCIAL_SUCCESS,
			userService.getBySocial(socialType, providerId)
		);
	}
}
