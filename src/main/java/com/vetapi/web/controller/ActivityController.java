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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activities")
@Tag(name = "Activities", description = "API para monitoreo de actividades del sistema")
public class ActivityController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Operation(
            summary = "Obtener actividades recientes del sistema",
            description = "Retorna las actividades más recientes del sistema incluyendo consultas, citas, usuarios y mascotas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Actividades obtenidas exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentActivities(
            @RequestParam(defaultValue = "10") int limit) {
        
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> activities = new HashMap<>();
        
        try {
            // Consultas recientes
            List<Map<String, Object>> recentConsultations = jdbcTemplate.queryForList(
                "SELECT c.id, c.reason, c.date, c.created_at, " +
                "p.name as pet_name, u.name as veterinarian_name " +
                "FROM consultations c " +
                "JOIN pets p ON c.pet_id = p.id " +
                "JOIN users u ON c.veterinarian_id = u.id " +
                "WHERE c.active = true " +
                "ORDER BY c.created_at DESC LIMIT ?", limit
            );
            activities.put("recent_consultations", recentConsultations);

            // Citas recientes
            List<Map<String, Object>> recentAppointments = jdbcTemplate.queryForList(
                "SELECT a.id, a.date_time, a.reason, a.status, a.created_at, " +
                "p.name as pet_name, u.name as veterinarian_name " +
                "FROM appointments a " +
                "JOIN pets p ON a.pet_id = p.id " +
                "JOIN users u ON a.veterinarian_id = u.id " +
                "WHERE a.active = true " +
                "ORDER BY a.created_at DESC LIMIT ?", limit
            );
            activities.put("recent_appointments", recentAppointments);

            // Nuevos usuarios registrados
            List<Map<String, Object>> newUsers = jdbcTemplate.queryForList(
                "SELECT id, name, last_name, email, role, created_at " +
                "FROM users " +
                "WHERE active = true " +
                "ORDER BY created_at DESC LIMIT ?", limit
            );
            activities.put("new_users", newUsers);

            // Nuevas mascotas registradas
            List<Map<String, Object>> newPets = jdbcTemplate.queryForList(
                "SELECT p.id, p.name, p.species, p.breed, p.created_at, " +
                "CONCAT(c.name, ' ', c.last_name) as owner_name " +
                "FROM pets p " +
                "JOIN customers c ON p.customer_id = c.id " +
                "WHERE p.active = true " +
                "ORDER BY p.created_at DESC LIMIT ?", limit
            );
            activities.put("new_pets", newPets);

            // Vacunaciones recientes
            List<Map<String, Object>> recentVaccinations = jdbcTemplate.queryForList(
                "SELECT v.id, v.vaccine_type, v.application_date, v.created_at, " +
                "p.name as pet_name, u.name as veterinarian_name " +
                "FROM vaccinations v " +
                "JOIN pets p ON v.pet_id = p.id " +
                "JOIN users u ON v.veterinarian_id = u.id " +
                "WHERE v.active = true " +
                "ORDER BY v.created_at DESC LIMIT ?", limit
            );
            activities.put("recent_vaccinations", recentVaccinations);

            // Estadísticas generales
            Map<String, Object> stats = new HashMap<>();
            
            // Conteos totales
            Integer totalConsultations = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM consultations WHERE active = true", Integer.class);
            Integer totalAppointments = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM appointments WHERE active = true", Integer.class);
            Integer totalUsers = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE active = true", Integer.class);
            Integer totalPets = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM pets WHERE active = true", Integer.class);
            Integer totalVaccinations = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM vaccinations WHERE active = true", Integer.class);

            stats.put("total_consultations", totalConsultations);
            stats.put("total_appointments", totalAppointments);
            stats.put("total_users", totalUsers);
            stats.put("total_pets", totalPets);
            stats.put("total_vaccinations", totalVaccinations);

            // Actividad del día actual
            String today = LocalDateTime.now().toLocalDate().toString();
            Integer todayConsultations = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM consultations WHERE active = true AND created_at::date = ?::date", 
                Integer.class, today);
            Integer todayAppointments = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM appointments WHERE active = true AND created_at::date = ?::date", 
                Integer.class, today);

            stats.put("today_consultations", todayConsultations);
            stats.put("today_appointments", todayAppointments);

            activities.put("statistics", stats);

            response.put("status", "success");
            response.put("message", "Actividades obtenidas exitosamente");
            response.put("timestamp", LocalDateTime.now());
            response.put("limit", limit);
            response.put("data", activities);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al obtener las actividades del sistema");
            response.put("error", e.getMessage());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(
            summary = "Obtener estadísticas del sistema",
            description = "Retorna estadísticas generales del sistema veterinario"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadísticas obtenidas exitosamente",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getSystemStats() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> stats = new HashMap<>();

            // Estadísticas por rol de usuario
            List<Map<String, Object>> usersByRole = jdbcTemplate.queryForList(
                "SELECT role, COUNT(*) as count FROM users WHERE active = true GROUP BY role");
            stats.put("users_by_role", usersByRole);

            // Actividad por mes (últimos 6 meses) - PostgreSQL compatible
            List<Map<String, Object>> monthlyActivity = jdbcTemplate.queryForList(
                "SELECT TO_CHAR(created_at, 'YYYY-MM') as month, COUNT(*) as consultations " +
                "FROM consultations WHERE active = true AND created_at >= NOW() - INTERVAL '6 months' " +
                "GROUP BY TO_CHAR(created_at, 'YYYY-MM') ORDER BY month DESC");
            stats.put("monthly_consultations", monthlyActivity);

            // Top 5 especies más atendidas
            List<Map<String, Object>> topSpecies = jdbcTemplate.queryForList(
                "SELECT p.species, COUNT(c.id) as consultation_count " +
                "FROM pets p " +
                "JOIN consultations c ON p.id = c.pet_id " +
                "WHERE p.active = true AND c.active = true " +
                "GROUP BY p.species ORDER BY consultation_count DESC LIMIT 5");
            stats.put("top_species", topSpecies);

            response.put("status", "success");
            response.put("message", "Estadísticas obtenidas exitosamente");
            response.put("timestamp", LocalDateTime.now());
            response.put("data", stats);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al obtener las estadísticas del sistema");
            response.put("error", e.getMessage());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(500).body(response);
        }
    }
} 