package com.carbon.ingestion.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request object for content ingestion")
public record IngestionRequest(
        @Schema(
                description = "The text content to be processed",
                example = "This is a sample text that needs to be analyzed by the Carbon AI platform.",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "text must not be blank")
        String text
) {
}

