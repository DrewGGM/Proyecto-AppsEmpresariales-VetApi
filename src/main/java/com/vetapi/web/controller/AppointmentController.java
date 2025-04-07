package com.vetapi.web.controller;

import com.vetapi.application.dto.appointment.AppointmentCreateDTO;
import com.vetapi.application.dto.appointment.AppointmentDTO;
import com.vetapi.application.dto.appointment.AppointmentUpdateDTO;
import com.vetapi.application.service.AppointmentService;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment", description = "API for managing veterinary appointments")
public class AppointmentController {
    private final AppointmentService service;

    @PostMapping
    @Operation(summary = "Create a new appointment",
            description = "Creates a new veterinary appointment in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Appointment successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid appointment data",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Unprocessable entity - validation failed",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<AppointmentDTO> create(
            @Parameter(description = "Appointment creation details", required = true)
            @Valid @RequestBody AppointmentCreateDTO createDTO) {
        try {
            return new ResponseEntity<>(service.save(createDTO), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing appointment",
            description = "Updates the details of an existing veterinary appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Appointment successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid appointment data",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Appointment not found",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Unprocessable entity - validation failed",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<AppointmentDTO> update(
            @Parameter(description = "Unique identifier of the appointment to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Appointment update details", required = true)
            @Valid @RequestBody AppointmentUpdateDTO updateDTO) {
        return ResponseEntity.ok(service.update(updateDTO, id));
    }

    @GetMapping
    @Operation(summary = "Get all appointments",
            description = "Retrieves a list of all veterinary appointments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved list of appointments",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "204",
                    description = "No appointments found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<AppointmentDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID",
            description = "Retrieves a specific appointment by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved appointment",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid appointment ID",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Appointment not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<AppointmentDTO> findById(
            @Parameter(description = "Unique identifier of the appointment", required = true)
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an appointment",
            description = "Removes an appointment from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Appointment successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Appointment not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Unique identifier of the appointment to delete", required = true)
            @PathVariable Long id) {
        return service.delete(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/idVet/{id}")
    @Operation(summary = "Get appointments by veterinarian",
            description = "Retrieves all appointments for a specific veterinarian")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved appointments",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Veterinarian not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<AppointmentDTO>> findByVeterinarian(
            @Parameter(description = "Unique identifier of the veterinarian", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findByVeterinarian(id));
    }

    @GetMapping("/idPet/{id}")
    @Operation(summary = "Get appointments by pet",
            description = "Retrieves all appointments for a specific pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved appointments",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Pet not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<AppointmentDTO>> findByPet(
            @Parameter(description = "Unique identifier of the pet", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findByPet(id));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get appointments by date",
            description = "Retrieves all appointments on a specific date and time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved appointments",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<AppointmentDTO>> findByDateTime(
            @Parameter(description = "Date and time of appointments", required = true)
            @PathVariable LocalDateTime date) {
        return ResponseEntity.ok(service.findByDate(date));
    }
}