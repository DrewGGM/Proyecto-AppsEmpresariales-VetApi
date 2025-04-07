package com.vetapi.web.controller;

import com.vetapi.application.dto.consultation.ConsultationCreateDTO;
import com.vetapi.application.dto.consultation.ConsultationDTO;
import com.vetapi.application.dto.consultation.ConsultationUpdateDTO;
import com.vetapi.application.dto.document.DocumentDTO;
import com.vetapi.application.dto.treatment.TreatmentCreateDTO;
import com.vetapi.application.dto.treatment.TreatmentDTO;
import com.vetapi.application.service.ConsultationService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/consultations")
@RequiredArgsConstructor
@Tag(name = "Consultation", description = "API for managing veterinary consultations")
public class ConsultationController {
    private final ConsultationService service;

    @PostMapping
    @Operation(summary = "Create a new consultation",
            description = "Creates a new veterinary consultation in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Consultation successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultationDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid consultation data",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Unprocessable entity - validation failed",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<ConsultationDTO> create(
            @Parameter(description = "Consultation creation details", required = true)
            @Valid @RequestBody ConsultationCreateDTO createDTO) {
        return new ResponseEntity<>(service.save(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing consultation",
            description = "Updates the details of an existing veterinary consultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Consultation successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultationDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid consultation data",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Consultation not found",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Unprocessable entity - validation failed",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<ConsultationDTO> update(
            @Parameter(description = "Unique identifier of the consultation to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Consultation update details", required = true)
            @Valid @RequestBody ConsultationUpdateDTO updateDTO) {
        return ResponseEntity.ok(service.update(updateDTO, id));
    }

    @GetMapping
    @Operation(summary = "Get all consultations",
            description = "Retrieves a list of all veterinary consultations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved list of consultations",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultationDTO.class))),
            @ApiResponse(responseCode = "204",
                    description = "No consultations found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<ConsultationDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get consultation by ID",
            description = "Retrieves a specific consultation by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved consultation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultationDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Consultation not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<ConsultationDTO> findById(
            @Parameter(description = "Unique identifier of the consultation", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a consultation",
            description = "Removes a consultation from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Consultation successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Consultation not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Unique identifier of the consultation to delete", required = true)
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pet/{id}")
    @Operation(summary = "Get consultations by pet",
            description = "Retrieves all consultations for a specific pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved consultations",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultationDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Pet not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<ConsultationDTO>> findByPet(
            @Parameter(description = "Unique identifier of the pet", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findByPet(id));
    }

    @GetMapping("/veterinarian/{id}")
    @Operation(summary = "Get consultations by veterinarian",
            description = "Retrieves all consultations conducted by a specific veterinarian")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved consultations",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultationDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Veterinarian not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<ConsultationDTO>> findByVeterinarian(
            @Parameter(description = "Unique identifier of the veterinarian", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findByVeterinarian(id));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get consultations by date",
            description = "Retrieves all consultations on a specific date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved consultations",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultationDTO.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<ConsultationDTO>> findByDate(
            @Parameter(description = "Date of consultations", required = true)
            @PathVariable LocalDate date) {
        return ResponseEntity.ok(service.findByDate(date));
    }

    @PostMapping("/{id}/treatments")
    @Operation(summary = "Add treatment to a consultation",
            description = "Adds a new treatment to an existing consultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Treatment successfully added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TreatmentDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid treatment data",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Consultation not found",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Unprocessable entity - validation failed",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<TreatmentDTO> addTreatment(
            @Parameter(description = "Unique identifier of the consultation", required = true)
            @PathVariable Long id,
            @Parameter(description = "Treatment creation details", required = true)
            @Valid @RequestBody TreatmentCreateDTO treatmentDTO) {
        return new ResponseEntity<>(service.addTreatment(id, treatmentDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/documents")
    @Operation(summary = "Add document to a consultation",
            description = "Uploads a document associated with a specific consultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Document successfully uploaded",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid file",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Consultation not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<DocumentDTO> addDocument(
            @Parameter(description = "Unique identifier of the consultation", required = true)
            @PathVariable Long id,
            @Parameter(description = "Document file to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(service.addDocument(id, file), HttpStatus.CREATED);
    }
}