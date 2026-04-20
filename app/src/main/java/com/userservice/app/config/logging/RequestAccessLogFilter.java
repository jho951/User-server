package com.userservice.app.config.logging;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.userservice.common.logging.LoggingHeaders;
import com.userservice.common.logging.LoggingMdcKeys;
import com.userservice.common.logging.SensitiveDataMasker;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
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
				"http_access method={} path={} query={} status={} durationMs={} forwardedFor={} requestId={} correlationId={} gatewayUserId={}",
				request.getMethod(),
				request.getRequestURI(),
				SensitiveDataMasker.maskQuery(request.getQueryString()),
				response.getStatus(),
				elapsed,
				request.getHeader(LoggingHeaders.X_FORWARDED_FOR),
				MDC.get(LoggingMdcKeys.REQUEST_ID),
				MDC.get(LoggingMdcKeys.CORRELATION_ID),
				SensitiveDataMasker.maskIdentifier(request.getHeader(LoggingHeaders.X_USER_ID))
			);
		}
	}
}
