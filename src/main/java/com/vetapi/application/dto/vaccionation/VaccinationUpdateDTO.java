package com.vetapi.application.dto.vaccionation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class VaccinationUpdateDTO {

    @NotBlank(message = " Vaccine Type is required")
    private String vaccineType;

    @NotNull(message = " Application date is required")
    private LocalDate applicationDate;

    private LocalDate nextApplicationDate;
    private String lotNumber;
    private String observations;
}
