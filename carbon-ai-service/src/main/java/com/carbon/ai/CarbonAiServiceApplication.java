package com.carbon.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.carbon.ai", "com.carbon.shared"})
public class CarbonAiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarbonAiServiceApplication.class, args);
    }
}

