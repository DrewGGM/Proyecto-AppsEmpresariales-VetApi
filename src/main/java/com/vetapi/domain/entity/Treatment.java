package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.SplittableRandom;

@Getter
@Setter
@NoArgsConstructor
public class Treatment extends BaseEntity {

    private  Pet pet;
    private Consultation consultation;
    private String medicine;
    private String dose;
    private String frequency;
    private LocalDate startDate;
    private  LocalDate endDate;
    private  boolean filled;
    private  String observations;

    public Treatment(Pet pet, Consultation consultation, String medicine, String dose, String frequency, LocalDate startDate, LocalDate endDate, boolean filled, String observations) {
        this.pet = pet;
        this.consultation = consultation;
        this.medicine = medicine;
        this.dose = dose;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.filled = filled;
        this.observations = observations;
    }
}
