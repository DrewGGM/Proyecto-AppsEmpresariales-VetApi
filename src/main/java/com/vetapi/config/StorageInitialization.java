package com.vetapi.config;

import com.vetapi.application.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageInitialization {

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            // Initialize storage directory on application startup
            storageService.init();
        };
    }
}