package com.vetapi.application.dto.vaccionation;

import com.vetapi.domain.entity.Pet;
import com.vetapi.domain.entity.User;
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
public class VaccinationDTO {
    private long id;
    private String petName;
    private long  petId;
    private String veterinarianName;
    private long veterinarianId;
    private String vaccineType;
    private LocalDate applicationDate;
    private LocalDate nextApplicationDate;
    private String lotNumber;
    private String observations;
    private boolean upToDate;
    private int daysUntilNextApplication;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
}
