package com.carbon.ai.service;

import com.carbon.ai.model.ProcessedContent;
import com.carbon.ai.repository.ProcessedContentRepository;
import com.carbon.shared.event.ContentEvent;
import com.carbon.shared.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiProcessingService {

    private final ChatModel chatModel;
    private final EmbeddingModel embeddingModel;
    private final ProcessedContentRepository repository;
    private final ExecutorService aiVirtualThreadExecutor;

    public void handleEvent(ContentEvent event) {
        aiVirtualThreadExecutor.submit(() -> processEvent(event));
    }

    void processEvent(ContentEvent event) {
        try {
            log.debug("Starting AI processing for event actionId={}", event.id());
            String prompt = """
                    Summarize the following text into exactly three concise bullet points.
                    Respond only with the bullet list.

                    Text:
                    %s
                    """.formatted(event.content());

            String summary = chatModel.call(prompt);
            log.debug("Generated summary for event actionId={}: {}", event.id(), summary);

            float[] embedding = embeddingModel.embed(summary);

            ProcessedContent entity = ProcessedContent.builder()
                    .originalId(event.id())
                    .summary(summary)
                    .embedding(embedding)
                    .build();

            repository.save(entity);
        } catch (Exception e) {
            log.error("Failed to process ContentUploadedEvent actionId={}", event.id(), e);
            throw new ApplicationException(
                    format("Failed to process ContentUploadedEvent actionId=%s", event.id()),
                    HttpStatus.BAD_REQUEST,
                    HttpStatus.BAD_REQUEST.value()
            );
        }
    }
}

