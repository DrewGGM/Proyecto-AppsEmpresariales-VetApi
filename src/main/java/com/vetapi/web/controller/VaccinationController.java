package com.vetapi.web.controller;

import com.vetapi.application.dto.vaccination.VaccinationCreateDTO;
import com.vetapi.application.dto.vaccination.VaccinationDTO;
import com.vetapi.application.dto.vaccination.VaccinationUpdateDTO;
import com.vetapi.application.service.VaccinationService;
import com.vetapi.domain.exception.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vaccinations")
@RequiredArgsConstructor
@Tag(name = "Vacunaciones", description = "API para la gestión de vacunaciones de mascotas")
public class VaccinationController {

    private final VaccinationService service;

    @PostMapping
    @Operation(summary = "Crear una nueva vacunación",
            description = "Registra una nueva vacunación con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Vacunación creada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VaccinationDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos - mascota o veterinario no encontrados",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Entidad no procesable - datos no cumplen validaciones",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<VaccinationDTO> create(@Valid @RequestBody VaccinationCreateDTO createDTO) {
        try {
            return new ResponseEntity<>(service.save(createDTO), HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una vacunación",
            description = "Actualiza los datos de una vacunación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Vacunación actualizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VaccinationDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos o formato de fecha incorrecto",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Vacunación no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Entidad no procesable - datos no cumplen validaciones",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<VaccinationDTO> update(
            @Parameter(description = "ID de la vacunación", required = true)
            @PathVariable Long id,
            @Valid @RequestBody VaccinationUpdateDTO updateDTO) {
        return ResponseEntity.ok(service.update(updateDTO,id));
    }

    @GetMapping
    @Operation(summary = "Obtener todas las vacunaciones",
            description = "Devuelve una lista de todas las vacunaciones registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de vacunaciones obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VaccinationDTO.class))),
            @ApiResponse(responseCode = "204",
                    description = "Lista vacía - No hay vacunaciones registradas",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<VaccinationDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener vacunación por ID",
            description = "Devuelve una vacunación específica según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Vacunación encontrada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VaccinationDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "ID inválido",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Vacunación no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<VaccinationDTO> findById(
            @Parameter(description = "ID de la vacunación", required = true)
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una vacunación",
            description = "Elimina una vacunación según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Vacunación eliminada correctamente",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Vacunación no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la vacunación", required = true)
            @PathVariable Long id) {
        return service.delete(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/dateNex/{date}")
    @Operation(summary = "Buscar vacunaciones por fecha de próxima aplicación",
            description = "Devuelve las vacunaciones programadas para una fecha específica de próxima aplicación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de vacunaciones obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VaccinationDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Formato de fecha inválido",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<VaccinationDTO>> findByNextApplicationDate(
            @Parameter(description = "Fecha de próxima aplicación (formato: YYYY-MM-DD)", required = true)
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.findByNextApplicationDate(date));
    }

    @GetMapping("/idPet/{id}")
    @Operation(summary = "Buscar vacunaciones por mascota",
            description = "Devuelve las vacunaciones realizadas a una mascota específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de vacunaciones obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VaccinationDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Mascota no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<VaccinationDTO>> findByPet(
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findByPet(id));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Buscar vacunaciones por fecha de aplicación",
            description = "Devuelve las vacunaciones realizadas en una fecha específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de vacunaciones obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VaccinationDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Formato de fecha inválido",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<VaccinationDTO>> findByDate(
            @Parameter(description = "Fecha de aplicación (formato: YYYY-MM-DD)", required = true)
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.findByDate(date));
    }
}