package com.vetapi.application.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetCreateDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private LocalDate birthDate;

    @NotBlank(message = "Species is required")
    private String species;

    private String breed;

    private String gender;

    private Float weight;

    private String photoUrl;

    @NotNull(message = "Customer ID is required")
    private Long customerId;
}