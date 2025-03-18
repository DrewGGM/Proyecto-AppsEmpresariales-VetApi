package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private LocalDateTime lastAccess;
    private List<Consultation> consultations = new ArrayList<>();
    private List<Vaccination> vaccinations = new ArrayList<>();

    // Verifica si el email tiene un formato válido
    public boolean hasValidEmail() {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    // Verifica si la contraseña cumple con los requisitos mínimos de seguridad
    public boolean hasSecurePassword() {
        return password != null && password.length() >= 6;
    }

    // Verifica si el usuario es administrador
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    // Verifica si el usuario es veterinario
    public boolean isVeterinarian() {
        return "VETERINARIAN".equalsIgnoreCase(role);
    }

    // Verifica si el usuario es recepcionista
    public boolean isReceptionist() {
        return "RECEPTIONIST".equalsIgnoreCase(role);
    }

    // Actualiza la fecha del último acceso al sistema
    public void updateLastAccess() {
        this.lastAccess = LocalDateTime.now();
    }

    // Agrega una consulta al usuario (veterinario)
    public void addConsultation(Consultation consultation) {
        if (consultation != null && !consultations.contains(consultation)) {
            consultations.add(consultation);
            consultation.setVeterinarian(this);
        }
    }

    // Agrega una vacunación al usuario (veterinario)
    public void addVaccination(Vaccination vaccination) {
        if (vaccination != null && !vaccinations.contains(vaccination)) {
            vaccinations.add(vaccination);
            vaccination.setVeterinarian(this);
        }
    }

    // Obtiene el nombre completo del usuario
    public String getFullName() {
        return name + " " + lastName;
    }
}