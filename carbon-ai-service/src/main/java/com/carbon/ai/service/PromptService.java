package com.carbon.ai.service;

import com.carbon.ai.config.PromptsProperties;
import com.carbon.ai.config.PromptCategory;
import com.carbon.ai.model.PromptType;
import com.carbon.shared.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromptService {

    private final PromptsProperties promptsProperties;

    public String getPrompt(String promptKey, PromptType promptType, Map<String, String> variables) {
        Map<String, String> category = getCategory(promptKey);
        PromptCategory promptCategory = convertToPromptCategory(category);
        String promptTemplate = promptCategory.getPrompt(promptType);

        if (promptTemplate == null) {
            throw new ApplicationException(
                    "No prompt template found for type: " + promptType,
                    INTERNAL_SERVER_ERROR,
                    INTERNAL_SERVER_ERROR.value()
            );
        }

        return substituteVariables(promptTemplate, variables);
    }

    private Map<String, String> getCategory(String promptKey) {
        Map<String, Map<String, String>> categories = promptsProperties.getCategories();

        if (categories == null) {
            throw new ApplicationException(
                    "No prompts configured",
                    INTERNAL_SERVER_ERROR,
                    INTERNAL_SERVER_ERROR.value()
            );
        }

        Map<String, String> category = categories.get(promptKey);

        if (category == null) {
            throw new ApplicationException(
                    "No prompts configured for key: " + promptKey,
                    INTERNAL_SERVER_ERROR,
                    INTERNAL_SERVER_ERROR.value()
            );
        }

        return category;
    }

    private PromptCategory convertToPromptCategory(Map<String, String> category) {
        PromptCategory promptCategory = new PromptCategory();
        promptCategory.setStandard(category.get("standard"));
        promptCategory.setConcise(category.get("concise"));
        promptCategory.setDetailed(category.get("detailed"));
        promptCategory.setTechnical(category.get("technical"));
        promptCategory.setSimple(category.get("simple"));
        return promptCategory;
    }

    private String substituteVariables(String template, Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }
}
