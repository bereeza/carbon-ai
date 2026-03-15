package com.carbon.ai.api;

import com.carbon.ai.dto.SystemInsights;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/api/v1")
@Tag(name = "Insights", description = "System insights API for processing statistics")
public interface InsightsAPI {

    @Operation(summary = "Get system insights", description = "Retrieve system processing statistics and insights")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved insights",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SystemInsights.class)
                    )
            )
    })
    @GetMapping(path = "/insights")
    ResponseEntity<SystemInsights> getInsights();
}
