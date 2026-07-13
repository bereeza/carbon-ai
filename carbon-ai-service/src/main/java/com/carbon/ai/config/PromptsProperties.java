package com.carbon.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "carbon.prompts")
public class PromptsProperties {
    private Map<String, Map<String, String>> categories;
}
