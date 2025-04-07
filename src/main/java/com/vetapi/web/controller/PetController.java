package com.vetapi.web.controller;

import com.vetapi.application.dto.pet.PetCreateDTO;
import com.vetapi.application.dto.pet.PetDTO;
import com.vetapi.application.dto.pet.PetListDTO;
import com.vetapi.application.dto.pet.PetUpdateDTO;
import com.vetapi.application.service.PetService;
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
@RequestMapping("/pets")
@RequiredArgsConstructor
@Tag(name = "Mascotas", description = "API para la gestión de mascotas")
public class PetController {

    private final PetService petService;

    @GetMapping
    @Operation(summary = "Obtener todas las mascotas",
            description = "Devuelve una lista resumida de todas las mascotas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de mascotas obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PetListDTO.class))),
            @ApiResponse(responseCode = "204",
                    description = "Lista vacía - No hay mascotas registradas",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<PetListDTO>> findAll() {
        return ResponseEntity.ok(petService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener mascota por ID",
            description = "Devuelve una mascota específica según su ID con información detallada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Mascota encontrada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PetDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Mascota no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<PetDTO> findById(
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(petService.findById(id));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Buscar mascotas por cliente",
            description = "Devuelve todas las mascotas que pertenecen a un cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de mascotas obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PetListDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Cliente no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<PetListDTO>> findByCustomerId(
            @Parameter(description = "ID del cliente", required = true)
            @PathVariable Long customerId) {
        return ResponseEntity.ok(petService.findByCustomerId(customerId));
    }

    @GetMapping("/species/{species}")
    @Operation(summary = "Buscar mascotas por especie",
            description = "Devuelve todas las mascotas de una especie específica (ej: perro, gato)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de mascotas obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PetListDTO.class))),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<PetListDTO>> findBySpecies(
            @Parameter(description = "Especie de la mascota (ej: perro, gato)", required = true)
            @PathVariable String species) {
        return ResponseEntity.ok(petService.findBySpecies(species));
    }

    @GetMapping("/breed/{breed}")
    @Operation(summary = "Buscar mascotas por raza",
            description = "Devuelve todas las mascotas de una raza específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de mascotas obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PetListDTO.class))),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<PetListDTO>> findByBreed(
            @Parameter(description = "Raza de la mascota", required = true)
            @PathVariable String breed) {
        return ResponseEntity.ok(petService.findByBreed(breed));
    }

    @GetMapping("/birth-after")
    @Operation(summary = "Buscar mascotas nacidas después de una fecha",
            description = "Devuelve todas las mascotas con fecha de nacimiento posterior a la especificada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de mascotas obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PetListDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Formato de fecha inválido",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<PetListDTO>> findByBirthDateAfter(
            @Parameter(description = "Fecha de nacimiento (formato: YYYY-MM-DD)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(petService.findByBirthDateAfter(date));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar mascotas por nombre",
            description = "Realiza una búsqueda de mascotas cuyo nombre contenga el texto especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de mascotas obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PetListDTO.class))),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<PetListDTO>> search(
            @Parameter(description = "Texto a buscar en el nombre de la mascota", required = true)
            @RequestParam String query) {
        return ResponseEntity.ok(petService.findByNameContaining(query));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva mascota",
            description = "Registra una nueva mascota con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Mascota creada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PetDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos o cliente no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Entidad no procesable - datos no cumplen validaciones",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<PetDTO> create(@Valid @RequestBody PetCreateDTO createDTO) {
        return new ResponseEntity<>(petService.create(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una mascota",
            description = "Actualiza los datos de una mascota existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Mascota actualizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PetDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos o formato incorrecto",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Mascota no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Entidad no procesable - datos no cumplen validaciones",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<PetDTO> update(
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long id,
            @Valid @RequestBody PetUpdateDTO updateDTO) {
        return ResponseEntity.ok(petService.update(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una mascota",
            description = "Elimina una mascota según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Mascota eliminada correctamente",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Mascota no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "409",
                    description = "Conflicto - No se puede eliminar porque tiene consultas asociadas",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/consultations/count")
    @Operation(summary = "Contar consultas de una mascota",
            description = "Retorna el número total de consultas médicas registradas para una mascota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Conteo obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "404",
                    description = "Mascota no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<Integer> countConsultations(
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(petService.countConsultations(id));
    }

    @GetMapping("/{id}/vaccinations/count")
    @Operation(summary = "Contar vacunaciones de una mascota",
            description = "Retorna el número total de vacunaciones registradas para una mascota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Conteo obtenido correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "404",
                    description = "Mascota no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<Integer> countVaccinations(
            @Parameter(description = "ID de la mascota", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(petService.countVaccinations(id));
    }
}