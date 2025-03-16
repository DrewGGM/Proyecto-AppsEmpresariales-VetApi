package com.vetapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// Controlador para la página principal de la API
@RestController
@RequestMapping("/")
public class HomeController {

    // Endpoint de bienvenida para verificar que la API esté funcionando
    @Operation(summary = "Endpoint de bienvenida", description = "Verifica que la API esté funcionando correctamente")
    @ApiResponse(responseCode = "200", description = "API en funcionamiento")
    @GetMapping
    public ResponseEntity<Map<String, Object>> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bienvenido a la API del Sistema Veterinario");
        response.put("status", "ACTIVO");
        response.put("version", "1.0.0");

        return ResponseEntity.ok(response);
    }
}