package com.vetapi.application.dto.consultation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationListDTO {
    private long id;
    private String petName;
    private String veterinarianName;
    private LocalDateTime date;
    private int treatmentCount;
    private int documentCount;
    private boolean isRecent;
    private String reason;
}
