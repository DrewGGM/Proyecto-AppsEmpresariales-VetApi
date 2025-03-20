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
public class AppointmentUpdateDTO {

    @NotNull(message = "Date time is required")
    private LocalDateTime dateTime;

    @NotBlank(message = "Reason is required")
    private String reason;

    @NotBlank(message = "Status is required")
    private String status;

    private boolean confirmed;
    private String observations;
}
