package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Pet extends BaseEntity {
    private String name;
    private LocalDate birthDate;
    private String species;
    private String breed;
    private String gender;
    private Float weight;
    private String photoUrl;
    private Customer customer;
    private List<Consultation> consultations = new ArrayList<>();
    private List<Vaccination> vaccinations = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();

    // Calcula la edad de la mascota en años
    public int calculateAge() {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // Verifica si la mascota es un adulto (más de 1 año)
    public boolean isAdult() {
        return calculateAge() >= 1;
    }

    public void addConsultation(Consultation consultation) {
        if (consultation != null && !consultations.contains(consultation)) {
            consultations.add(consultation);
            // Verifica si ya existe la relación inversa para evitar recursión infinita
            if (consultation.getPet() != this) {
                consultation.setPet(this);
            }
        }
    }

    public void removeConsultation(Consultation consultation) {
        if (consultation != null && consultations.contains(consultation)) {
            consultations.remove(consultation);
            // Verifica si existe la relación inversa para evitar recursión infinita
            if (consultation.getPet() == this) {
                consultation.setPet(null);
            }
        }
    }

    // Agrega una cita a la mascota
    public void addAppointment(Appointment appointment) {
        if (appointment != null && !appointments.contains(appointment)) {
            appointments.add(appointment);
            appointment.setPet(this);
        }
    }
}