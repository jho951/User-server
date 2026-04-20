package com.userservice.app.config.logging;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.userservice.app.common.logging.LoggingHeaders;
import com.userservice.app.common.logging.LoggingMdcKeys;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestMdcFilter extends OncePerRequestFilter {

	private final String serviceName;

	public RequestMdcFilter(@Value("${spring.application.name:user-service}") String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getRequestURI().startsWith("/actuator");
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String requestId = resolveRequestId(request);
		String correlationId = resolveCorrelationId(request);
		response.setHeader(LoggingHeaders.REQUEST_ID, requestId);
		response.setHeader(LoggingHeaders.CORRELATION_ID, correlationId);

		MDC.put(LoggingMdcKeys.REQUEST_ID, requestId);
		MDC.put(LoggingMdcKeys.CORRELATION_ID, correlationId);
		MDC.put(LoggingMdcKeys.TRACE_ID, resolveTraceId(request, correlationId));
		MDC.put(LoggingMdcKeys.HTTP_METHOD, request.getMethod());
		MDC.put(LoggingMdcKeys.REQUEST_URI, request.getRequestURI());
		MDC.put(LoggingMdcKeys.CLIENT_IP, resolveClientIp(request));
		MDC.put(LoggingMdcKeys.SERVICE_NAME, serviceName);

		try {
			filterChain.doFilter(request, response);
		} finally {
			MDC.remove(LoggingMdcKeys.REQUEST_ID);
			MDC.remove(LoggingMdcKeys.CORRELATION_ID);
			MDC.remove(LoggingMdcKeys.TRACE_ID);
			MDC.remove(LoggingMdcKeys.HTTP_METHOD);
			MDC.remove(LoggingMdcKeys.REQUEST_URI);
			MDC.remove(LoggingMdcKeys.CLIENT_IP);
			MDC.remove(LoggingMdcKeys.SERVICE_NAME);
		}
	}

	private String resolveRequestId(HttpServletRequest request) {
		String requestId = request.getHeader(LoggingHeaders.REQUEST_ID);
		if (requestId == null || requestId.isBlank()) {
			return UUID.randomUUID().toString();
		}
		return requestId;
	}

	private String resolveCorrelationId(HttpServletRequest request) {
		String correlationId = request.getHeader(LoggingHeaders.CORRELATION_ID);
		if (correlationId == null || correlationId.isBlank()) {
			return UUID.randomUUID().toString();
		}
		return correlationId;
	}

	private String resolveTraceId(HttpServletRequest request, String fallback) {
		String traceparent = request.getHeader(LoggingHeaders.TRACEPARENT);
		if (traceparent == null || traceparent.isBlank()) {
			return fallback;
		}
		String[] parts = traceparent.split("-");
		return parts.length >= 2 && !parts[1].isBlank() ? parts[1] : fallback;
	}

	private String resolveClientIp(HttpServletRequest request) {
		String forwardedFor = request.getHeader(LoggingHeaders.X_FORWARDED_FOR);
		if (forwardedFor != null && !forwardedFor.isBlank()) {
			int comma = forwardedFor.indexOf(',');
			return comma > -1 ? forwardedFor.substring(0, comma).trim() : forwardedFor.trim();
		}
		String remoteAddr = request.getRemoteAddr();
		return remoteAddr == null ? "unknown" : remoteAddr;
	}
}
