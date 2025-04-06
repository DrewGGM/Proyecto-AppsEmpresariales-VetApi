package com.vetapi.web.controller;

import com.vetapi.application.dto.appointment.AppointmentCreateDTO;
import com.vetapi.application.dto.appointment.AppointmentDTO;
import com.vetapi.application.dto.appointment.AppointmentUpdateDTO;
import com.vetapi.application.dto.treatment.TreatmentCreateDTO;
import com.vetapi.application.dto.treatment.TreatmentDTO;
import com.vetapi.application.dto.treatment.TreatmentUpdateDTO;
import com.vetapi.application.service.TreatmentService;
import com.vetapi.domain.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/treatments")
@RequiredArgsConstructor
public class TreatmentController {
    private TreatmentService service;
    @PostMapping
    public ResponseEntity<TreatmentDTO> create(@Valid @RequestBody TreatmentCreateDTO createDTO) {
        try {
            return new ResponseEntity<>(service.save(createDTO), HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<TreatmentDTO> update(@PathVariable Long id, @Valid @RequestBody TreatmentUpdateDTO updateDTO) {
        return ResponseEntity.ok(service.update(updateDTO,id));
    }

    @GetMapping
    public ResponseEntity<List<TreatmentDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<TreatmentDTO> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/idPet/{id}")
    public ResponseEntity<List<TreatmentDTO>> findByPet(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByPet(id));
    }

    @GetMapping("/idConsultation/{id}")
    public ResponseEntity<List<TreatmentDTO>> findByConsultation(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByConsultation(id));
    }





}
