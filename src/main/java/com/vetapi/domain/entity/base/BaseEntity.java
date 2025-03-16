package com.vetapi.domain.entity.base;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Clase base para todas las entidades del dominio con atributos comunes
@Getter
@Setter
public abstract class BaseEntity {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;

    // Activa la entidad
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    // Desactiva la entidad (borrado lógico)
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    // Inicializa la entidad cuando se crea
    protected void initialize() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
    }
}