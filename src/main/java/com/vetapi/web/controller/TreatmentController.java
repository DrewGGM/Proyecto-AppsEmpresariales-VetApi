package com.vetapi.web.controller;

import com.vetapi.application.dto.treatment.TreatmentCreateDTO;
import com.vetapi.application.dto.treatment.TreatmentDTO;
import com.vetapi.application.dto.treatment.TreatmentUpdateDTO;
import com.vetapi.application.service.TreatmentService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treatments")
@RequiredArgsConstructor
@Tag(name = "Tratamientos", description = "API para la gestión de tratamientos médicos de mascotas")
public class TreatmentController {
    private final TreatmentService service;

    @PostMapping
    @Operation(summary = "Crear un nuevo tratamiento",
            description = "Registra un nuevo tratamiento médico con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Tratamiento creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos - mascota o consulta no encontradas",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Entidad no procesable - datos no cumplen validaciones",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<TreatmentDTO> create(@Valid @RequestBody TreatmentCreateDTO createDTO) {
        try {
            return new ResponseEntity<>(service.save(createDTO), HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un tratamiento",
            description = "Actualiza los datos de un tratamiento médico existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Tratamiento actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos o fechas incorrectas",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Tratamiento no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Entidad no procesable - datos no cumplen validaciones",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<TreatmentDTO> update(
            @Parameter(description = "ID del tratamiento", required = true)
            @PathVariable Long id,
            @Valid @RequestBody TreatmentUpdateDTO updateDTO) {
        return ResponseEntity.ok(service.update(updateDTO,id));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los tratamientos",
            description = "Devuelve una lista de todos los tratamientos médicos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de tratamientos obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "204",
                    description = "Lista vacía - No hay tratamientos registrados",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<TreatmentDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tratamiento por ID",
            description = "Devuelve un tratamiento médico específico según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Tratamiento encontrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "ID inválido",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Tratamiento no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<TreatmentDTO> findById(
            @Parameter(description = "ID del tratamiento", required = true)
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un tratamiento",
            description = "Elimina un tratamiento médico según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Tratamiento eliminado correctamente",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Tratamiento no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del tratamiento", required = true)
            @PathVariable Long id) {
        return service.delete(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/idPet/{id}")
    @Operation(summary = "Buscar tratamientos por mascota",
            description = "Devuelve los tratamientos médicos asociados a una mascota específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de tratamientos obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Mascota no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<TreatmentDTO>> findByPet(
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findByPet(id));
    }

    @GetMapping("/idConsultation/{id}")
    @Operation(summary = "Buscar tratamientos por consulta",
            description = "Devuelve los tratamientos médicos asociados a una consulta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de tratamientos obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Consulta no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<TreatmentDTO>> findByConsultation(
            @Parameter(description = "ID de la consulta", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findByConsultation(id));
    }
}