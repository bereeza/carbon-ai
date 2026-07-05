package com.carbon.ingestion.api;

import com.carbon.ingestion.mapping.ContentMapper;
import com.carbon.ingestion.service.IngestionService;
import com.carbon.shared.dto.ContentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IngestionController implements IngestionAPI {

    private final IngestionService ingestionService;
    private final ContentMapper contentMapper;

    @Override
    public ResponseEntity<ContentResponse> ingest(@Valid @RequestBody IngestionRequest request) {
        var event = ingestionService.ingest(request);
        var response = contentMapper.mapContentEvent(event);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}

