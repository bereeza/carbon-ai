package com.carbon.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContentResponse(
        @JsonProperty("actionId") UUID actionId,
        @JsonProperty("content") String content,
        @JsonProperty("createdAt") LocalDateTime createdAt
) {
}
