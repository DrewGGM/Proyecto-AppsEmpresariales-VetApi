package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Vaccination  extends BaseEntity {

    private Pet pet;
    private User veterinarian;
    private String vaccineType;
    private LocalDate applicationDate;
    private LocalDate nextApplicationDate;
    private String lotNumber;
    private String observations;

public boolean isUpToDate(){
    if (nextApplicationDate == null){
        return true;
    }
    return (LocalDate.now().isBefore(nextApplicationDate));
}

public int daysUntilNextApplication(){
    if(nextApplicationDate == null){
        return -1;
    }
  return(Period.between(LocalDate.now(),nextApplicationDate).getDays());
}

}
