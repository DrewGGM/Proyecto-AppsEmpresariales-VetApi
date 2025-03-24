package com.vetapi.application.dto.consultation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationCreateDTO {

    @NotNull(message = "Id pet is required")
    private long petId;

    @NotNull(message = "Id veterinarian is required")
    private long veterinarianId;

    @NotNull(message = "date is required")
    private LocalDateTime date;

    @NotBlank(message = "reason is required")
    private String reason;

    private String diagnosis;
    private String observations;
}
