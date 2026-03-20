package com.api.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT 검증에 필요한 보안 설정값을 바인딩합니다.
 *
 * @param enabled JWT 보안 활성화 여부
 * @param issuer issuer claim 값
 * @param secret HMAC 시크릿
 * @param audience aud claim 값
 * @param internalScope 내부 호출용 scope 값
 */
@ConfigurationProperties(prefix = "security.jwt")
public record JwtSecurityProperties(
	boolean enabled,
	String issuer,
	String secret,
	String audience,
	String internalScope
) {
}
