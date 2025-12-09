package com.api.user.controller;

import com.api.user.domain.User;
import com.api.user.dto.UserRequestDto;
import com.api.user.service.UserServiceInterface;
import com.core.dto.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserAdminController {

	private final UserServiceInterface userService;

	public UserAdminController(UserServiceInterface userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<BaseResponse<List<User>>> getUsers() {
		List<User> users = userService.findAll();
		return ResponseEntity.ok(BaseResponse.ok(users));
	}

	@PostMapping
	public ResponseEntity<BaseResponse<User>> createUser(@RequestBody UserRequestDto request) {
		User created = userService.create(request);
		return ResponseEntity.ok(BaseResponse.ok(created, "관리자 계정이 생성되었습니다."));
	}

	@PutMapping("/{id}")
	public ResponseEntity<BaseResponse<User>> updateUser(
		@PathVariable Long id,
		@RequestBody UserRequestDto request
	) {
		User updated = userService.update(id, request);
		return ResponseEntity.ok(BaseResponse.ok(updated, "관리자 계정이 수정되었습니다."));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<BaseResponse<Void>> deleteUser(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.ok(BaseResponse.ok(null, "관리자 계정이 삭제되었습니다."));
	}
}
