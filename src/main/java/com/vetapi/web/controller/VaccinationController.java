package com.vetapi.web.controller;


import com.vetapi.application.dto.vaccination.VaccinationCreateDTO;
import com.vetapi.application.dto.vaccination.VaccinationDTO;
import com.vetapi.application.dto.vaccination.VaccinationUpdateDTO;
import com.vetapi.application.service.VaccinationService;
import com.vetapi.domain.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/Vaccinations")
@RequiredArgsConstructor
public class VaccinationController {

    private final VaccinationService service;
    @PostMapping
    public ResponseEntity<VaccinationDTO> create(@Valid @RequestBody VaccinationCreateDTO createDTO) {
        try {
            return new ResponseEntity<>(service.save(createDTO), HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<VaccinationDTO> update(@PathVariable Long id, @Valid @RequestBody VaccinationUpdateDTO updateDTO) {
        return ResponseEntity.ok(service.update(updateDTO,id));
    }

    @GetMapping
    public ResponseEntity<List<VaccinationDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<VaccinationDTO> findById(@PathVariable Long id) {
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

    @GetMapping("/dateNex/{date}")
    public ResponseEntity<List<VaccinationDTO>> findByNextApplicationDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(service.findByNextApplicationDate(date));
    }
    @GetMapping("/idPet/{id}")
    public ResponseEntity<List<VaccinationDTO>> findByPet(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByPet(id));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<VaccinationDTO>> findByDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(service.findByDate(date));
    }




}
