package com.vetapi.web.controller;

import com.vetapi.application.dto.user.UserCreateDTO;
import com.vetapi.application.dto.user.UserDTO;
import com.vetapi.application.dto.user.UserListDTO;
import com.vetapi.application.dto.user.UserUpdateDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID",
            description = "Devuelve un usuario específico según su ID con información detallada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuario encontrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<UserDTO> findById(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener usuario por email",
            description = "Devuelve un usuario específico según su dirección de correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuario encontrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<UserDTO> findByEmail(
            @Parameter(description = "Email del usuario", required = true)
            @PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario",
            description = "Registra un nuevo usuario con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Usuario creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos o el email ya está registrado",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Entidad no procesable - datos no cumplen validaciones",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<UserDTO> create(
            @Valid @RequestBody UserCreateDTO createDTO) {
        return new ResponseEntity<>(userService.create(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario",
            description = "Actualiza los datos de un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Usuario actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos o el email ya está registrado por otro usuario",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Entidad no procesable - datos no cumplen validaciones",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<UserDTO> update(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO updateDTO) {
        return ResponseEntity.ok(userService.update(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario (lógico)",
            description = "Desactiva un usuario según su ID (eliminación lógica)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Usuario desactivado correctamente",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/permanent")
    @Operation(summary = "Eliminar un usuario permanentemente",
            description = "Elimina definitivamente un usuario de la base de datos (no se puede recuperar)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Usuario eliminado permanentemente",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "409",
                    description = "Conflicto - No se puede eliminar porque tiene datos asociados",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<Void> hardDelete(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        userService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }
}