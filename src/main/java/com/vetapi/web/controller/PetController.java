package com.vetapi.web.controller;

import com.vetapi.application.dto.pet.PetCreateDTO;
import com.vetapi.application.dto.pet.PetDTO;
import com.vetapi.application.dto.pet.PetListDTO;
import com.vetapi.application.dto.pet.PetUpdateDTO;
import com.vetapi.application.service.PetService;
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
public class PetController {

    private final PetService petService;

    @GetMapping
    public ResponseEntity<List<PetListDTO>> findAll() {
        return ResponseEntity.ok(petService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.findById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PetListDTO>> findByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(petService.findByCustomerId(customerId));
    }

    @GetMapping("/species/{species}")
    public ResponseEntity<List<PetListDTO>> findBySpecies(@PathVariable String species) {
        return ResponseEntity.ok(petService.findBySpecies(species));
    }

    @GetMapping("/breed/{breed}")
    public ResponseEntity<List<PetListDTO>> findByBreed(@PathVariable String breed) {
        return ResponseEntity.ok(petService.findByBreed(breed));
    }

    @GetMapping("/birth-after")
    public ResponseEntity<List<PetListDTO>> findByBirthDateAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(petService.findByBirthDateAfter(date));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PetListDTO>> search(@RequestParam String query) {
        return ResponseEntity.ok(petService.findByNameContaining(query));
    }

    @PostMapping
    public ResponseEntity<PetDTO> create(@Valid @RequestBody PetCreateDTO createDTO) {
        return new ResponseEntity<>(petService.create(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDTO> update(@PathVariable Long id, @Valid @RequestBody PetUpdateDTO updateDTO) {
        return ResponseEntity.ok(petService.update(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/consultations/count")
    public ResponseEntity<Integer> countConsultations(@PathVariable Long id) {
        return ResponseEntity.ok(petService.countConsultations(id));
    }

    @GetMapping("/{id}/vaccinations/count")
    public ResponseEntity<Integer> countVaccinations(@PathVariable Long id) {
        return ResponseEntity.ok(petService.countVaccinations(id));
    }
}