package com.api.config.security.gateway;

import java.util.UUID;

/**
 * gateway가 전달한 사용자 컨텍스트를 표현합니다.
 *
 * @param userId 사용자 식별자
 * @param status 사용자 상태 코드(A/P/S/D)
 */
public record GatewayUserPrincipal(
	UUID userId,
	String status
) {
}
