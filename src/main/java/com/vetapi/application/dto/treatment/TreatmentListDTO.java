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
public class TreatmentListDTO {
    private long id;
    private String petName;
    private String medicine;
    private String dosage;
    private String frequency;
    private LocalDate startDate;
    private  LocalDate endDate;
    private  boolean  isActive;
    private  boolean completed;

}
