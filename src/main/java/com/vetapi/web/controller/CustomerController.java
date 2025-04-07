package com.vetapi.web.controller;

import com.vetapi.application.dto.customer.CustomerCreateDTO;
import com.vetapi.application.dto.customer.CustomerDTO;
import com.vetapi.application.dto.customer.CustomerListDTO;
import com.vetapi.application.dto.customer.CustomerUpdateDTO;
import com.vetapi.application.service.CustomerService;
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

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "API for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @Operation(summary = "Get all customers",
            description = "Retrieves a list of all customers in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved list of customers",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerListDTO.class))),
            @ApiResponse(responseCode = "204",
                    description = "No customers found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<CustomerListDTO>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID",
            description = "Retrieves a specific customer by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved customer",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<CustomerDTO> findById(
            @Parameter(description = "Unique identifier of the customer", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get customer by email",
            description = "Retrieves a specific customer by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved customer",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<CustomerDTO> findByEmail(
            @Parameter(description = "Email address of the customer", required = true)
            @PathVariable String email) {
        return ResponseEntity.ok(customerService.findByEmail(email));
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers",
            description = "Search customers by name or last name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved matching customers",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerListDTO.class))),
            @ApiResponse(responseCode = "204",
                    description = "No customers found matching the search query",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<CustomerListDTO>> search(
            @Parameter(description = "Search query to find customers by name or last name", required = true)
            @RequestParam String query) {
        return ResponseEntity.ok(customerService.findByNameOrLastName(query));
    }

    @PostMapping
    @Operation(summary = "Create a new customer",
            description = "Creates a new customer in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Customer successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid customer data",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Unprocessable entity - validation failed",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<CustomerDTO> create(
            @Parameter(description = "Customer creation details", required = true)
            @Valid @RequestBody CustomerCreateDTO createDTO) {
        return new ResponseEntity<>(customerService.create(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing customer",
            description = "Updates the details of an existing customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Customer successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid customer data",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Unprocessable entity - validation failed",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<CustomerDTO> update(
            @Parameter(description = "Unique identifier of the customer to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Customer update details", required = true)
            @Valid @RequestBody CustomerUpdateDTO updateDTO) {
        return ResponseEntity.ok(customerService.update(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer",
            description = "Removes a customer from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Customer successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Unique identifier of the customer to delete", required = true)
            @PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pets/count")
    @Operation(summary = "Count customer's pets",
            description = "Retrieves the number of pets owned by a specific customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved number of pets",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "404",
                    description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Integer> countPets(
            @Parameter(description = "Unique identifier of the customer", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.countPets(id));
    }
}