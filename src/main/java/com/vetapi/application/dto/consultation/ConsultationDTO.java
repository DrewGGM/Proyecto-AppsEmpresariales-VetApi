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
public class ConsultationDTO {
    private long id;
    private String petName;
    private long petId;
    private String veterinarianName;
    private long veterinarianId;
    private LocalDateTime date;
    private String reason;
    private String diagnosis;
    private String observations;
    private int treatmentCount;
    private int documentCount;
    private boolean isRecent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
