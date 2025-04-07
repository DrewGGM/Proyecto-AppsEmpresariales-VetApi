package com.vetapi.web.controller;

import com.vetapi.application.dto.consultation.ConsultationCreateDTO;
import com.vetapi.application.dto.consultation.ConsultationDTO;
import com.vetapi.application.dto.consultation.ConsultationUpdateDTO;
import com.vetapi.application.dto.document.DocumentDTO;
import com.vetapi.application.dto.treatment.TreatmentCreateDTO;
import com.vetapi.application.dto.treatment.TreatmentDTO;
import com.vetapi.application.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/consultations")
@RequiredArgsConstructor
public class ConsultationController {
    private final ConsultationService service;

    @PostMapping
    public ResponseEntity<ConsultationDTO> create(@Valid @RequestBody ConsultationCreateDTO createDTO) {
        return new ResponseEntity<>(service.save(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultationDTO> update(@PathVariable Long id, @Valid @RequestBody ConsultationUpdateDTO updateDTO) {
        return ResponseEntity.ok(service.update(updateDTO, id));
    }

    @GetMapping
    public ResponseEntity<List<ConsultationDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pet/{id}")
    public ResponseEntity<List<ConsultationDTO>> findByPet(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByPet(id));
    }

    @GetMapping("/veterinarian/{id}")
    public ResponseEntity<List<ConsultationDTO>> findByVeterinarian(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByVeterinarian(id));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ConsultationDTO>> findByDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(service.findByDate(date));
    }

    @PostMapping("/{id}/treatments")
    public ResponseEntity<TreatmentDTO> addTreatment(
            @PathVariable Long id,
            @Valid @RequestBody TreatmentCreateDTO treatmentDTO) {
        return new ResponseEntity<>(service.addTreatment(id, treatmentDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<DocumentDTO> addDocument(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(service.addDocument(id, file), HttpStatus.CREATED);
    }
}