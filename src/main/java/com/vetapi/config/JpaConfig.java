package com.vetapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Habilita la auditoría automática para las entidades JPA
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}