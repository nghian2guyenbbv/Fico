package vn.com.tpf.microservices.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring")
@Data
public class BscScoreConfig {
    Map<String, Map<String, String>> bsc_score_body;
}
