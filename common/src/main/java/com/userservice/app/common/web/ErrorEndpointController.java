package com.userservice.app.common.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userservice.app.common.logging.LoggingHeaders;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/** 서블릿 컨테이너가 /error로 전달한 에러 요청을 JSON 응답으로 변환합니다. */
@RestController
public class ErrorEndpointController implements ErrorController {

	@RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		HttpStatus status = resolveStatus(request);
		String errorMessage = resolveMessage(request, status);

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", errorMessage);

		putIfPresent(body, "path", stringAttribute(request, RequestDispatcher.ERROR_REQUEST_URI));
		putIfPresent(body, "requestId", header(request, LoggingHeaders.REQUEST_ID));
		putIfPresent(body, "correlationId", header(request, LoggingHeaders.CORRELATION_ID));

		return ResponseEntity.status(status)
			.contentType(MediaType.APPLICATION_JSON)
			.body(body);
	}

	private HttpStatus resolveStatus(HttpServletRequest request) {
		Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		int rawStatus = statusCode instanceof Integer code ? code : 500;

		HttpStatus status = HttpStatus.resolve(rawStatus);
		return status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status;
	}

	private String resolveMessage(HttpServletRequest request, HttpStatus status) {
		String message = stringAttribute(request, RequestDispatcher.ERROR_MESSAGE);
		return message == null ? status.getReasonPhrase() : message;
	}

	private String stringAttribute(HttpServletRequest request, String name) {
		Object value = request.getAttribute(name);
		if (!(value instanceof String text) || text.isBlank()) {
			return null;
		}
		return text;
	}

	private String header(HttpServletRequest request, String name) {
		String value = request.getHeader(name);
		if (value == null || value.isBlank()) {
			return null;
		}
		return value;
	}

	private void putIfPresent(Map<String, Object> body, String key, String value) {
		if (value != null) {
			body.put(key, value);
		}
	}
}
