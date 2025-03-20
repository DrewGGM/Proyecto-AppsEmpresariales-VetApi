package com.vetapi.application.dto.treatment;

import com.vetapi.domain.entity.Consultation;
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
public class TreatmentCreateDTO {

    @NotNull(message = "Id pet is required")
    private String petId;

    @NotNull(message = "Id consultation is required")
    private long consultationId;

    @NotBlank(message = "medicine is required")
    private String medicine;

    @NotBlank(message = "dosage is required")
    private String dosage;

    @NotBlank(message = "frequency is required")
    private String frequency;

    @NotBlank(message = "startDate is required")
    private LocalDate startDate;

    private  LocalDate endDate;
    private  String observations;
}
