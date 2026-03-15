package com.carbon.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "System insights containing processing statistics")
public class SystemInsights {
    
    @Schema(description = "Total number of processed documents", example = "1500")
    private long totalProcessedDocuments;
    
    @Schema(description = "Average length of document summaries", example = "245.5")
    private double averageSummaryLength;
    
    @Schema(description = "List of top keywords from processed documents")
    private List<String> topKeywords;
}
