package com.vetapi.application.dto.treatment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentDTO {
    private long id;
    private String petName;
    private long petId;
    private long consultationId;
    private String medicine;
    private String dosage;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean completed;
    private String observations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
    private boolean isActive;
    private int getDurationDays;

}
