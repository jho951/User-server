package com.userservice.common.logging;

import java.util.Set;

public final class SensitiveDataMasker {

	private static final Set<String> SENSITIVE_QUERY_KEYS = Set.of(
		"email",
		"providerId",
		"provider_id",
		"token",
		"accessToken",
		"refreshToken",
		"password"
	);

	private SensitiveDataMasker() {
	}

	public static String maskIdentifier(String value) {
		if (value == null || value.isBlank()) {
			return "unknown";
		}
		String trimmed = value.trim();
		int atIndex = trimmed.indexOf('@');
		if (atIndex > 1) {
			return trimmed.charAt(0) + "***" + trimmed.substring(atIndex);
		}
		if (trimmed.length() <= 2) {
			return "***";
		}
		return trimmed.charAt(0) + "***" + trimmed.charAt(trimmed.length() - 1);
	}

	public static String maskQuery(String query) {
		if (query == null || query.isBlank()) {
			return query;
		}

		StringBuilder masked = new StringBuilder();
		String[] pairs = query.split("&");
		for (int i = 0; i < pairs.length; i++) {
			if (i > 0) {
				masked.append('&');
			}
			String pair = pairs[i];
			int separator = pair.indexOf('=');
			if (separator < 0) {
				masked.append(pair);
				continue;
			}
			String key = pair.substring(0, separator);
			String value = pair.substring(separator + 1);
			masked.append(key)
				.append('=')
				.append(SENSITIVE_QUERY_KEYS.contains(key) ? maskIdentifier(value) : value);
		}
		return masked.toString();
	}
}
