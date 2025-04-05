package com.vetapi.application.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetListDTO {
    private Long id;
    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;
    private String customerName;
    private int age;
    private boolean active;
}