package com.carbon.ingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.carbon.ingestion", "com.carbon.shared"})
public class CarbonIngestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarbonIngestionServiceApplication.class, args);
    }
}

