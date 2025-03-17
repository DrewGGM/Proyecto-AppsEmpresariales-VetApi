package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Consultation extends BaseEntity {

    private Pet pet;
    private User veterinarian;
    private LocalDateTime date;
    private String reason;
    private String diagnosis;
    private String observations;

    public Consultation(Pet pet, User veterinarian, LocalDateTime date, String reason, String diagnosis, String observations) {
        this.pet = pet;
        this.veterinarian = veterinarian;
        this.date = date;
        this.reason = reason;
        this.diagnosis = diagnosis;
        this.observations = observations;
        initialize();
    }


}
