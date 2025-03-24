package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity {
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private List<Pet> pets = new ArrayList<>();

    public void addPet(Pet pet) {
        if (pet != null && !pets.contains(pet)) {
            pets.add(pet);
            // Verifica si ya existe la relación inversa para evitar recursión infinita
            if (pet.getCustomer() != this) {
                pet.setCustomer(this);
            }
        }
    }

    public void removePet(Pet pet) {
        if (pet != null && pets.contains(pet)) {
            pets.remove(pet);
            // Verifica si existe la relación inversa para evitar recursión infinita
            if (pet.getCustomer() == this) {
                pet.setCustomer(null);
            }
        }
    }

    // Verifica si el email tiene un formato válido
    public boolean hasValidEmail() {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    // Verifica si el teléfono tiene un formato válido
    public boolean hasValidPhone() {
        return phone != null && phone.matches("^[0-9]{10}$");
    }
}