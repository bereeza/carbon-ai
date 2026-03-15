package com.carbon.shared.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContentEvent(
        @JsonProperty("id") UUID id,
        @JsonProperty("content") String content,
        @JsonProperty("createdAt") LocalDateTime createdAt
) {
}

