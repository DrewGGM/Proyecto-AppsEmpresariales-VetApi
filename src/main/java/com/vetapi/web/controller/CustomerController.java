package com.vetapi.web.controller;

import com.vetapi.application.dto.customer.CustomerCreateDTO;
import com.vetapi.application.dto.customer.CustomerDTO;
import com.vetapi.application.dto.customer.CustomerListDTO;
import com.vetapi.application.dto.customer.CustomerUpdateDTO;
import com.vetapi.application.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerListDTO>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(customerService.findByEmail(email));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerListDTO>> search(@RequestParam String query) {
        return ResponseEntity.ok(customerService.findByNameOrLastName(query));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> create(@Valid @RequestBody CustomerCreateDTO createDTO) {
        return new ResponseEntity<>(customerService.create(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @Valid @RequestBody CustomerUpdateDTO updateDTO) {
        return ResponseEntity.ok(customerService.update(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pets/count")
    public ResponseEntity<Integer> countPets(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.countPets(id));
    }
}