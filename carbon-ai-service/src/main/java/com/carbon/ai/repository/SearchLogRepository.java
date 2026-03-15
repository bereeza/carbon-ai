package com.carbon.ai.repository;

import com.carbon.ai.model.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {

    @Query(value = """
            SELECT word, COUNT(*) as frequency
            FROM (
                SELECT regexp_split_to_table(lower(query), '[^a-z0-9]+') as word
                FROM (
                    SELECT query
                    FROM search_logs
                    ORDER BY searched_at DESC
                    LIMIT :limit
                ) recent_searches
            ) words
            WHERE length(word) >= :minLength
            AND word NOT IN (:stopWords)
            GROUP BY word
            ORDER BY frequency DESC
            LIMIT :topLimit
            """, nativeQuery = true)
    List<Object[]> getTopFrequentWords(
            @Param("limit") int limit,
            @Param("minLength") int minLength,
            @Param("stopWords") String stopWords,
            @Param("topLimit") int topLimit
    );
}
