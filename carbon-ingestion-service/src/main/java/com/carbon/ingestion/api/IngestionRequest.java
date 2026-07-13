package com.carbon.ingestion.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for content ingestion")
public record IngestionRequest(
        @Schema(
                description = "The text content to be processed",
                example = "This is a sample text that needs to be analyzed by the Carbon AI platform.",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "text must not be blank")
        @NotNull(message = "text must not be null")
        @Size(max = 1024, message = "text must be at most 1024 characters")
        @Pattern(regexp = "^.*$", message = "text must not contain invalid characters")
        String text
) {
}

