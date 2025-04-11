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
public class AppointmentListDTO {
    private long id;
    private String petName;
    private String veterinarianName;
    private LocalDateTime dateTime;
    private String reason;
    private String status;
    private boolean confirmed;
    private boolean isToday;
}
