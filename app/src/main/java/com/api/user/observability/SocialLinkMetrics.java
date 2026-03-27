package com.api.user.observability;

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
			.description("Total number of social link requests")
			.register(meterRegistry);
		this.conflictsTotal = Counter.builder("social_link_conflicts_total")
			.description("Total number of social link unique conflicts")
			.register(meterRegistry);
		this.createTotal = Counter.builder("social_link_create_total")
			.description("Total number of newly created social links")
			.register(meterRegistry);
		this.existingTotal = Counter.builder("social_link_existing_total")
			.description("Total number of requests resolved by existing social links")
			.register(meterRegistry);
		this.latencyMs = DistributionSummary.builder("social_link_latency_ms")
			.description("Social link request latency in milliseconds")
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
