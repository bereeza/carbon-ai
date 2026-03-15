package com.carbon.ingestion.api;

import com.carbon.shared.dto.ContentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Content Ingestion", description = "API for ingesting content into the Carbon AI platform")
@RequestMapping(path = "/api/v1")
public interface IngestionAPI {

    @Operation(
            summary = "Ingest content for processing",
            description = "Submit text content to the Carbon AI platform for processing and analysis."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Content accepted for processing",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContentResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request - validation errors",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Validation failed\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Rate limit exceeded - too many requests",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"status\": 429, \"error\": \"Rate limit exceeded\", \"message\": \"Rate limit exceeded. Available tokens: 1.5, required: 2\"}")
                    )
            )
    })
    @PostMapping(path = "/ingest")
    ResponseEntity<ContentResponse> ingest(
            @Parameter(description = "Content ingestion request with text and source information", required = true)
            @Valid @RequestBody IngestionRequest request
    );
}
