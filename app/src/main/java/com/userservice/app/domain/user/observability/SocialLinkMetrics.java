package com.userservice.app.domain.user.observability;

import java.time.Duration;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class SocialLinkMetrics {

	private final Counter requestsTotal;
	private final Counter conflictsTotal;
	private final Counter createTotal;
	private final Counter existingTotal;
	private final DistributionSummary latencyMs;

	public SocialLinkMetrics(MeterRegistry meterRegistry) {
		this.requestsTotal = Counter.builder("social_link_requests_total")
			.description("소셜 링크 요청 총 횟수")
			.register(meterRegistry);
		this.conflictsTotal = Counter.builder("social_link_conflicts_total")
			.description("소셜 링크 유니크 제약 충돌 총 횟수")
			.register(meterRegistry);
		this.createTotal = Counter.builder("social_link_create_total")
			.description("새로 생성된 소셜 링크 총 횟수")
			.register(meterRegistry);
		this.existingTotal = Counter.builder("social_link_existing_total")
			.description("기존 소셜 링크로 처리된 요청 총 횟수")
			.register(meterRegistry);
		this.latencyMs = DistributionSummary.builder("social_link_latency_ms")
			.description("소셜 링크 요청 처리 시간")
			.baseUnit("milliseconds")
			.register(meterRegistry);
	}

	public void incrementRequests() {
		requestsTotal.increment();
	}

	public void incrementConflicts() {
		conflictsTotal.increment();
	}

	public void incrementCreate() {
		createTotal.increment();
	}

	public void incrementExisting() {
		existingTotal.increment();
	}

	public void recordLatency(Duration duration) {
		latencyMs.record(duration.toMillis());
	}
}
