package com.vetapi.application.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private long id;
    private String petName;
    private long petId;
    private String veterinarianName;
    private long veterinarianId;
    private LocalDateTime dateTime;
    private String reason;
    private String status;
    private boolean confirmed;
    private String observations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
    private boolean isToday;
    private boolean isPending;
}
