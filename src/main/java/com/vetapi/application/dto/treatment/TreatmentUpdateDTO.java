package com.vetapi.application.dto.treatment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentUpdateDTO {


    @NotBlank(message = "medicine is required")
    private String medicine;

    @NotBlank(message = "dosage is required")
    private String dosage;

    @NotBlank(message = "frequency is required")
    private String frequency;

    @NotBlank(message = "startDate is required")
    private LocalDate startDate;

    private boolean completed;
    private LocalDate endDate;
    private String observations;
}
