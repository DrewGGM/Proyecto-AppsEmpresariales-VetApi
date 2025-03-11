# VetAPI

API REST para la gestión de clínicas veterinarias implementada con Spring Boot siguiendo los principios de Domain-Driven Design (DDD).

## Descripción del Proyecto

Este sistema permite gestionar:
- Propietarios y mascotas
- Citas y consultas médicas
- Vacunaciones y tratamientos
- Notificaciones y recordatorios

## Arquitectura

El proyecto sigue una arquitectura basada en Domain-Driven Design (DDD) con las siguientes capas:

- **Capa de Dominio**: Contiene las entidades y reglas de negocio
- **Capa de Aplicación**: Coordina la ejecución de tareas y dirige las entidades de dominio
- **Capa de Infraestructura**: Proporciona capacidades técnicas que respaldan las capas superiores
- **Capa de Interfaces**: Convierte peticiones web en llamadas al sistema

## Tecnologías

- Java 17
- Spring Boot 3.4.3
- Gradle (Groovy)
- MySQL 8.0
- MapStruct
- Lombok
- OpenAPI/Swagger

## Requisitos

- JDK 17
- MySQL 8.0
- Gradle 7.6+

## Configuración

1. Clonar el repositorio
2. Configurar la base de datos en `application.yml`
3. Ejecutar `./gradlew clean build`
4. Ejecutar `./gradlew bootRun`

## Documentación API

La documentación de la API está disponible en:
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- OpenAPI Docs: `http://localhost:8080/api/api-docs`

## Estructura del Proyecto