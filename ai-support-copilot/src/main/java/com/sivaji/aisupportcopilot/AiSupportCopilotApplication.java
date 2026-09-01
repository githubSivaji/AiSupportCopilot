package com.sivaji.aisupportcopilot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AiSupportCopilotApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiSupportCopilotApplication.class, args);
    }

}
