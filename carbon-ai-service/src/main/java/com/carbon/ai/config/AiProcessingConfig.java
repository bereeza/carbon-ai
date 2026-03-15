package com.carbon.ai.config;

import com.carbon.ai.service.AiProcessingService;
import com.carbon.shared.event.ContentEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Configuration
public class AiProcessingConfig {

    @Bean(destroyMethod = "shutdown")
    public ExecutorService aiVirtualThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Consumer<ContentEvent> rawContentConsumer(AiProcessingService aiProcessingService) {
        return aiProcessingService::handleEvent;
    }
}

