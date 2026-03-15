package com.carbon.ingestion.service;

import com.carbon.ingestion.api.IngestionRequest;
import com.carbon.shared.event.ContentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngestionService {

    private static final String RAW_CONTENT_BINDING = "rawContent-out-0";

    private final StreamBridge streamBridge;

    public ContentEvent ingest(IngestionRequest request) {
        var event = new ContentEvent(
                UUID.randomUUID(),
                request.text(),
                LocalDateTime.now()
        );

        streamBridge.send(RAW_CONTENT_BINDING, event);
        return event;
    }
}

