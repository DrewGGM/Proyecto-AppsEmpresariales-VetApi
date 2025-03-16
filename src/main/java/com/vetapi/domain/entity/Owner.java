package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


//Entidad de dominio que representa a un cliente/propietario de mascotas
@Getter
@Setter
@NoArgsConstructor
public class Owner extends BaseEntity {
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private List<Pet> pets = new ArrayList<>();


    //Constructor con atributos obligatorios
    public Owner(String name, String lastName, String email, String phone) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        initialize();
    }


    //Método para agregar una mascota al propietario
    public void addPet(Pet pet) {
        if (pet != null && !pets.contains(pet)) {
            pets.add(pet);
            pet.setOwner(this);
        }
    }


    //Método para eliminar una mascota del propietario
    public void removePet(Pet pet) {
        if (pet != null && pets.contains(pet)) {
            pets.remove(pet);
            pet.setOwner(null);
        }
    }


    //Obtiene el nombre completo del propietario
    public String getFullName() {
        return name + " " + lastName;
    }
}