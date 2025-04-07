package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Treatment extends BaseEntity {

    private Pet pet;
    private Consultation consultation;
    private String medicine;
    private String dosage;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean completed;
    private String observations;


    public boolean isActive() {
        if (!this.isCompleted() && super.isActive()) {
            if (this.endDate == null) {
                return true;
            }
            return LocalDate.now().isBefore(this.endDate) || LocalDate.now().isEqual(this.endDate);
        }
        return false;
    }

    public int getDurationDays() {
        if (startDate == null || endDate == null) {
            return -1;
        }
        return (Period.between(startDate, endDate).getDays());
    }

    public void markAsCompleted() {
        this.completed = true;
    }

}
