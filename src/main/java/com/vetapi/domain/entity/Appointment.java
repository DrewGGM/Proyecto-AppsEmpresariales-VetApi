package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Appointment extends BaseEntity {

    private Pet pet;
    private User veterinarian;
    private LocalDateTime dateTime;
    private String reason;
    private String status;
    private boolean confirmed;
    private String observations;

    public Appointment(Pet pet, User veterinarian, LocalDateTime dateTime, String reason, String status, boolean confirmed, String observations) {
        this.pet = pet;
        this.veterinarian = veterinarian;
        this.dateTime = dateTime;
        this.reason = reason;
        this.status = status;
        this.confirmed = confirmed;
        this.observations = observations;
    }

}
