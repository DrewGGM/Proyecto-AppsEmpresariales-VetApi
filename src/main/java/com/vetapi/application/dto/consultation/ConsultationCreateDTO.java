package com.vetapi.application.dto.consultation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationCreateDTO {

    @NotNull(message = "Id pet is required")
    private String petId;

    @NotNull(message = "Id veterinarian is required")
    private long veterinarian_id;

    @NotNull(message = "date is required")
    private LocalDateTime date;

    @NotBlank(message = "reason is required")
    private String reason;

    private String diagnosis;
    private String observations;
}
