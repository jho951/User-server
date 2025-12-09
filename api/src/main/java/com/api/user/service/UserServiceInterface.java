package com.api.user.service;

import com.api.user.domain.User;
import com.api.user.dto.UserRequestDto;

import java.util.List;

public interface UserServiceInterface {

	/**
	 * 모든 관리자 계정 조회
	 */
	List<User> findAll();

	/**
	 * ID로 관리자 계정 조회 (없으면 예외)
	 */
	User findByIdOrThrow(Long id);

	/**
	 * 관리자 계정 생성
	 */
	User create(UserRequestDto request);

	/**
	 * 관리자 계정 수정
	 */
	User update(Long id, UserRequestDto request);

	/**
	 * 관리자 계정 삭제
	 */
	void delete(Long id);
}
