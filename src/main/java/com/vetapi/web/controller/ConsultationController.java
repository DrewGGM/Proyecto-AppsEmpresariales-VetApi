package com.vetapi.web.controller;

import com.vetapi.application.dto.consultation.ConsultationCreateDTO;
import com.vetapi.application.dto.consultation.ConsultationDTO;
import com.vetapi.application.dto.consultation.ConsultationUpdateDTO;
import com.vetapi.application.service.ConsultationService;
import com.vetapi.domain.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/consultations")
@RequiredArgsConstructor
public class ConsultationController {
    private ConsultationService service;

    @PostMapping
    public ResponseEntity<ConsultationDTO> create(@Valid @RequestBody ConsultationCreateDTO createDTO) {
        try {
            return new ResponseEntity<>(service.save(createDTO), HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ConsultationDTO> update(@PathVariable Long id, @Valid @RequestBody ConsultationUpdateDTO updateDTO) {
        return ResponseEntity.ok(service.update(updateDTO,id));
    }

    @GetMapping
    public ResponseEntity<List<ConsultationDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ConsultationDTO> findById(@PathVariable Long id) {
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

    @GetMapping("/idVet/{id}")
    public ResponseEntity<List<ConsultationDTO>> findByVeterinarian(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByVeterinarian(id));
    }
    @GetMapping("/idPet/{id}")
    public ResponseEntity<List<ConsultationDTO>> findByPet(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByPet(id));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ConsultationDTO>> findByDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(service.findByDate(date));
    }



}
