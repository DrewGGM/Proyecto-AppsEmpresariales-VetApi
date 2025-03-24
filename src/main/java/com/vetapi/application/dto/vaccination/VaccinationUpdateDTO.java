package com.vetapi.application.dto.vaccination;

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
public class VaccinationUpdateDTO {

    @NotBlank(message = " Vaccine Type is required")
    private String vaccineType;

    @NotNull(message = " Application date is required")
    private LocalDate applicationDate;

    private LocalDate nextApplicationDate;
    private String lotNumber;
    private String observations;
}