package com.vetapi.application.dto.pet;

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
public class PetDTO {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String species;
    private String breed;
    private String gender;
    private Float weight;
    private String photoUrl;
    private String customerName;
    private Long customerId;
    private int consultationCount;
    private int vaccinationCount;
    private int appointmentCount;
    private int age;
    private boolean isAdult;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
}