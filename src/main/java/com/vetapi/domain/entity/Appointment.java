package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment extends BaseEntity {

    private Pet pet;
    private User veterinarian;
    private LocalDateTime dateTime;
    private String reason;
    private String status;
    private boolean confirmed;
    private String observations;

   public void confirm(){
       this.confirmed = true;
   }

   public void complete(){
       this.status = "COMPLETED";
   }

    public void cancel(){
        this.status = "CANCELLED";
    }

    public boolean isPending(){
       if (dateTime == null){
           return false;
       }
if (dateTime.isBefore(LocalDateTime.now())){
    return true;
}
return false;
    }

    public boolean  isToday(){
       if (dateTime == null){
           return false;
       }
       return (dateTime.toLocalDate().equals(LocalDate.now()));
    }
}
