package com.api.config.governance;

import com.auditlog.api.AuditSink;
import com.auditlog.core.FileAuditSink;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserGovernanceAuditConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "auditlog", name = "enabled", havingValue = "true", matchIfMissing = true)
    public AuditSink userAuditSink(
            @Value("${auditlog.service-name:user-service}") String serviceName,
            @Value("${auditlog.env:local}") String env,
            @Value("${auditlog.file-path:./logs/audit.log}") String filePath
    ) {
        return new FileAuditSink(Path.of(filePath), serviceName, env);
    }
}
