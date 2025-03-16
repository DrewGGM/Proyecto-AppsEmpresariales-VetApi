package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;


//Entidad de dominio que representa una mascota
@Getter
@Setter
@NoArgsConstructor
public class Pet extends BaseEntity {
    private String name;
    private LocalDate birthDate;
    private String species;
    private String breed;
    private String gender;
    private Float weight;
    private String photoUrl;
    private Owner owner;

    //Constructor con atributos obligatorios
    public Pet(String name, String species, Owner owner) {
        this.name = name;
        this.species = species;
        this.owner = owner;
        initialize();
    }


    //Calcula la edad de la mascota en años
    public Integer calculateAge() {
        if (birthDate == null) {
            return null;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }


    //Verifica si la mascota necesita chequeo de acuerdo a su edad
    public boolean needsCheckup() {
        Integer age = calculateAge();
        if (age == null) {
            return false;
        }

        // Las mascotas mayores necesitan chequeos más frecuentes
        return age >= 7;
    }
}
