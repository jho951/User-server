package com.api.user.service;

import java.util.UUID;

import com.api.user.constant.UserSocialType;
import com.api.user.dto.UserRequest;
import com.api.user.dto.UserResponse;

public interface UserService {

	UserResponse.UserCreateResponse signup(UserRequest.UserSignupRequest request);

	UserResponse.UserDetailResponse create(UserRequest.UserCreateRequest request);

	UserResponse.UserSocialResponse createSocial(UserRequest.UserSocialCreateRequest request);

	UserResponse.UserDetailResponse updateStatus(UUID userId, UserRequest.UserStatusUpdateRequest request);

	UserResponse.UserDetailResponse get(UUID userId);

	UserResponse.UserDetailResponse getByEmail(String email);

	UserResponse.UserDetailResponse getBySocial(UserSocialType socialType, String providerId);
}
