package com.vetapi.web.controller;

import com.vetapi.application.dto.user.*;
import com.vetapi.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "API para la gestión de usuarios del sistema")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios",
            description = "Devuelve una lista resumida de todos los usuarios registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de usuarios obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserListDTO.class))),
            @ApiResponse(responseCode = "204",
                    description = "Lista vacía - No hay usuarios registrados",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<UserListDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar usuarios con filtros",
            description = "Devuelve una lista paginada de usuarios con filtros opcionales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de usuarios obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<?> searchUsers(
            @Parameter(description = "Término de búsqueda")
            @RequestParam(required = false) String search,
            @Parameter(description = "Filtrar por rol")
            @RequestParam(required = false) String role,
            @Parameter(description = "Filtrar por estado activo")
            @RequestParam(required = false) Boolean active,
            @Parameter(description = "Número de página (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página")
            @RequestParam(defaultValue = "10") int size) {

        // If no params provided, return simple list for backward compatibility
        if (search == null && role == null && active == null && page == 0 && size == 10) {
            return ResponseEntity.ok(userService.findAll());
        }

        // Otherwise return paginated results
        Page<UserListDTO> results = userService.searchUsers(search, role, active, page, size);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID",
            description = "Devuelve un usuario específico según su ID con información detallada")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener usuario por email",
            description = "Devuelve un usuario específico según su dirección de correo electrónico")
    public ResponseEntity<UserDTO> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/{userId}/activity")
    @Operation(summary = "Obtener actividad del usuario",
            description = "Devuelve el historial de actividades recientes de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Actividad obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserActivityDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<UserActivityDTO>> getUserActivity(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserActivity(userId));
    }

    @GetMapping("/{userId}/stats")
    @Operation(summary = "Obtener estadísticas del usuario",
            description = "Devuelve las estadísticas de un veterinario (consultas, vacunaciones, citas)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Estadísticas obtenidas correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserStatsDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<UserStatsDTO> getUserStats(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserStats(userId));
    }

    @PostMapping("/{userId}/photo")
    @Operation(summary = "Subir foto de perfil",
            description = "Sube una nueva foto de perfil para el usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Foto subida correctamente",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400",
                    description = "Archivo inválido",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<Map<String, Object>> uploadPhoto(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long userId,
            @Parameter(description = "Archivo de imagen", required = true)
            @RequestParam("file") MultipartFile file) {
        Map<String, Object> response = userService.uploadPhoto(userId, file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario",
            description = "Registra un nuevo usuario con los datos proporcionados")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserCreateDTO createDTO) {
        return new ResponseEntity<>(userService.create(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario",
            description = "Actualiza los datos de un usuario existente")
    public ResponseEntity<UserDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO updateDTO) {
        return ResponseEntity.ok(userService.update(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario (lógico)",
            description = "Desactiva un usuario según su ID (eliminación lógica)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/permanent")
    @Operation(summary = "Eliminar un usuario permanentemente",
            description = "Elimina definitivamente un usuario de la base de datos")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        userService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }
}