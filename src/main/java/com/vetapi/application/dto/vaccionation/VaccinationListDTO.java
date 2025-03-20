package com.vetapi.application.dto.vaccionation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationListDTO {
    private long id;
    private String petName;
    private String veterinarianName;
    private LocalDate applicationDate;
    private LocalDate nextApplicationDate;
    private boolean upToDate;
}
