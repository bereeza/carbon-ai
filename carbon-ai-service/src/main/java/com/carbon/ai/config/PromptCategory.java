package com.carbon.ai.config;

import com.carbon.ai.model.PromptType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PromptCategory {
    private String standard;
    private String concise;
    private String detailed;
    private String technical;
    private String simple;

    public String getPrompt(PromptType promptType) {
        String promptTemplate = switch (promptType) {
            case STANDARD -> standard;
            case CONCISE -> concise;
            case DETAILED -> detailed;
            case TECHNICAL -> technical;
            case SIMPLE -> simple;
        };

        if (promptTemplate == null) {
            log.warn("Prompt type {} not found, falling back to STANDARD", promptType);
            promptTemplate = standard;
        }

        return promptTemplate;
    }
}
