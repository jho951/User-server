package com.api.config.security.gateway;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * gateway가 전달한 사용자 컨텍스트 헤더를 인증 정보로 변환합니다.
 */
@Component
public class GatewayUserContextFilter extends OncePerRequestFilter {

	private static final String HEADER_USER_ID = "X-User-Id";
	private static final String HEADER_USER_STATUS = "X-User-Status";

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			filterChain.doFilter(request, response);
			return;
		}

		String userIdHeader = request.getHeader(HEADER_USER_ID);
		if (userIdHeader == null || userIdHeader.isBlank()) {
			filterChain.doFilter(request, response);
			return;
		}

		UUID userId;
		try {
			userId = UUID.fromString(userIdHeader.trim());
		}
		catch (IllegalArgumentException ignored) {
			filterChain.doFilter(request, response);
			return;
		}

		String status = request.getHeader(HEADER_USER_STATUS);
		GatewayUserPrincipal principal = new GatewayUserPrincipal(userId, status);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			principal,
			null,
			List.of()
		);
		authentication.setDetails(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}
}
