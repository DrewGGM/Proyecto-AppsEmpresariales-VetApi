package com.vetapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@Tag(name = "Home", description = "API principal del sistema veterinario")
public class HomeController {

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
}