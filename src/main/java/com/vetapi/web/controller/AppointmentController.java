package com.vetapi.web.controller;

import com.vetapi.application.dto.appointment.AppointmentCreateDTO;
import com.vetapi.application.dto.appointment.AppointmentDTO;
import com.vetapi.application.dto.appointment.AppointmentUpdateDTO;
import com.vetapi.application.dto.pet.PetCreateDTO;
import com.vetapi.application.dto.pet.PetDTO;
import com.vetapi.application.dto.pet.PetListDTO;
import com.vetapi.application.dto.pet.PetUpdateDTO;
import com.vetapi.application.service.AppointmentService;
import com.vetapi.domain.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService service;

    @PostMapping
    public ResponseEntity<AppointmentDTO> create(@Valid @RequestBody AppointmentCreateDTO createDTO) {
        try {
            return new ResponseEntity<>(service.save(createDTO), HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> update(@PathVariable Long id, @Valid @RequestBody AppointmentUpdateDTO updateDTO) {
        return ResponseEntity.ok(service.update(updateDTO,id));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> findById(@PathVariable Long id) {
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
    public ResponseEntity<List<AppointmentDTO>> findByVeterinarian(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByVeterinarian(id));
    }
    @GetMapping("/idPet/{id}")
    public ResponseEntity<List<AppointmentDTO>> findByPet(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByPet(id));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<AppointmentDTO>> findByDateTime(@PathVariable LocalDateTime date) {
        return ResponseEntity.ok(service.findByDate(date));
    }








}
