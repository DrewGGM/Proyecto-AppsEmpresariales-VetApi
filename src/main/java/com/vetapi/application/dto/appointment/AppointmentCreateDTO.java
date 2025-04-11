package com.vetapi.application.dto.appointment;


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
public class AppointmentCreateDTO {

    @NotNull(message = "Id pet is required")
    private long petId;

    @NotNull(message = "Id veterinarian is required")
    private long veterinarianId;

    @NotNull(message = "Date time is required")
    private LocalDateTime dateTime;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String observations;
}

