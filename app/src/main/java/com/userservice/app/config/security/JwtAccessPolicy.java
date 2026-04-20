package com.userservice.app.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import com.userservice.app.domain.user.constant.UserStatus;

@Component("jwtAccessPolicy")
/**
 * JWT의 상태 및 scope 기반 접근 정책을 검사합니다.
 */
public class JwtAccessPolicy {

	private static final String ACTIVE_STATUS_AUTHORITY = "STATUS_ACTIVE";

	/**
	 * 인증된 사용자의 상태가 DB 코드 기준 ACTIVE(A)인지 확인합니다.
	 *
	 * @param authentication 인증 정보
	 * @return ACTIVE(A) 상태 여부
	 */
	public boolean hasActiveStatus(Authentication authentication) {
		if (authentication == null) {
			return false;
		}

		if (authentication.getAuthorities().stream()
			.anyMatch(grantedAuthority -> ACTIVE_STATUS_AUTHORITY.equals(grantedAuthority.getAuthority()))) {
			return true;
		}

		if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
			String status = jwtAuthenticationToken.getToken().getClaimAsString("status");
			return UserStatus.ACTIVE.getCode().equalsIgnoreCase(status);
		}

		return false;
	}
}
