package com.api.config.logging;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestAccessLogFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(RequestAccessLogFilter.class);

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return uri.startsWith("/actuator");
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		long startedAt = System.currentTimeMillis();

		try {
			filterChain.doFilter(request, response);
		} finally {
			long elapsed = System.currentTimeMillis() - startedAt;
			log.info(
				"http_access method={} path={} query={} status={} durationMs={} forwardedFor={} requestId={} gatewayUserId={}",
				request.getMethod(),
				request.getRequestURI(),
				request.getQueryString(),
				response.getStatus(),
				elapsed,
				request.getHeader("X-Forwarded-For"),
				request.getHeader("X-Request-Id"),
				request.getHeader("X-User-Id")
			);
		}
	}
}
