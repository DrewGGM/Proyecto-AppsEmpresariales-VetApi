package com.vetapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@Tag(name = "Home", description = "API principal del sistema veterinario")
public class HomeController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Operation(
            summary = "Endpoint de bienvenida",
            description = "Verifica que la API esté funcionando correctamente y muestra información básica del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "API en funcionamiento",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class,
                                    description = "Información del estado del sistema"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bienvenido a la API del Sistema Veterinario");
        response.put("status", "ACTIVO");
        response.put("version", "1.0.0");

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Health check del backend",
            description = "Verifica que el backend esté funcionando correctamente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Backend funcionando correctamente",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error en el backend",
                    content = @Content
            )
    })
    @GetMapping("/health/backend")
    public ResponseEntity<Map<String, Object>> backendHealthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Backend");
        response.put("status", "OK");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Conexión con el backend exitosa");
        
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Health check de la base de datos",
            description = "Verifica que la conexión con la base de datos esté funcionando correctamente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Conexión con base de datos exitosa",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error de conexión con la base de datos",
                    content = @Content
            )
    })
    @GetMapping("/health/database")
    public ResponseEntity<Map<String, Object>> databaseHealthCheck() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Realizar una consulta simple para verificar la conexión
            String result = jdbcTemplate.queryForObject("SELECT 1", String.class);
            
            response.put("service", "Database");
            response.put("status", "OK");
            response.put("timestamp", LocalDateTime.now());
            response.put("message", "Conexión con la base de datos exitosa");
            response.put("database_type", "PostgreSQL");
            response.put("test_query_result", result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("service", "Database");
            response.put("status", "ERROR");
            response.put("timestamp", LocalDateTime.now());
            response.put("message", "Error en la conexión con la base de datos");
            response.put("error", e.getMessage());
            
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(
            summary = "Health check completo del sistema",
            description = "Verifica el estado general del sistema incluyendo backend y base de datos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sistema funcionando correctamente",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Algunos servicios no están disponibles",
                    content = @Content
            )
    })
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> systemHealthCheck() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> services = new HashMap<>();
        boolean allServicesHealthy = true;
        
        // Verificar backend
        Map<String, Object> backendStatus = new HashMap<>();
        backendStatus.put("status", "OK");
        backendStatus.put("message", "Backend funcionando correctamente");
        services.put("backend", backendStatus);
        
        // Verificar base de datos
        Map<String, Object> databaseStatus = new HashMap<>();
        try {
            jdbcTemplate.queryForObject("SELECT 1", String.class);
            databaseStatus.put("status", "OK");
            databaseStatus.put("message", "Base de datos funcionando correctamente");
        } catch (Exception e) {
            databaseStatus.put("status", "ERROR");
            databaseStatus.put("message", "Error en la conexión con la base de datos");
            databaseStatus.put("error", e.getMessage());
            allServicesHealthy = false;
        }
        services.put("database", databaseStatus);
        
        response.put("timestamp", LocalDateTime.now());
        response.put("overall_status", allServicesHealthy ? "HEALTHY" : "DEGRADED");
        response.put("services", services);
        response.put("system", "Sistema Veterinario VetAPI");
        response.put("version", "1.0.0");
        
        return ResponseEntity.status(allServicesHealthy ? 200 : 503).body(response);
    }
}