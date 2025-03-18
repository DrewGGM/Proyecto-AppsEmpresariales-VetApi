package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Vaccination  extends BaseEntity {

    private Pet pet;
    private User veterinarian;
    private String vaccineType;
    private LocalDate dateApplication;
    private LocalDate nextApplicationDate;
    private String batch;
    private String observations;

    public Vaccination(Pet pet, User veterinarian, String vaccineType, LocalDate dateApplication, LocalDate nextApplicationDate, String batch, String observations) {
        this.pet = pet;
        this.veterinarian = veterinarian;
        this.vaccineType = vaccineType;
        this.dateApplication = dateApplication;
        this.nextApplicationDate = nextApplicationDate;
        this.batch = batch;
        this.observations = observations;
    }
}
