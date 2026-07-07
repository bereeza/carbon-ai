package com.carbon.ai.repository;

import com.carbon.ai.model.ProcessedContent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProcessedContentRepository extends JpaRepository<ProcessedContent, Long> {

    @Query(value = """
        SELECT *, embedding <=> cast(:embedding as vector) as distance
        FROM carbon.processed_content
        ORDER BY distance
        """, nativeQuery = true)
    List<ProcessedContent> findBySimilarity(
            @Param("embedding") float[] embedding,
            Pageable pageable
    );

    @Query(value = """
           SELECT AVG(LENGTH(summary)) 
           FROM processed_content
           """, nativeQuery = true)
    Double getAverageSummaryLength();

    @Query(value = """
            SELECT word
            FROM (
                SELECT regexp_split_to_table(lower(summary), '[^a-z0-9]+') as word
                FROM processed_content
                WHERE summary IS NOT NULL
            ) words
            WHERE length(word) >= 3
            GROUP BY word
            ORDER BY COUNT(*) DESC
            LIMIT 10
            """, nativeQuery = true)
    List<String> getTopKeywords();
}

