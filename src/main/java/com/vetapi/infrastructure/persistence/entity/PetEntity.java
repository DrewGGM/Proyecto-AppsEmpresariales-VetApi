package com.vetapi.infrastructure.persistence.entity;

import com.vetapi.infrastructure.persistence.entity.base.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad JPA que representa una mascota en la base de datos
 */
@Entity
@Table(name = "mascota")
@Getter
@Setter
@NoArgsConstructor
public class PetEntity extends BaseJpaEntity {

    @Column(name = "nombre", nullable = false)
    private String name;

    @Column(name = "fechaNacimiento")
    private LocalDate birthDate;

    @Column(name = "especie", nullable = false)
    private String species;

    @Column(name = "raza")
    private String breed;

    @Column(name = "sexo")
    private String gender;

    @Column(name = "peso")
    private Float weight;

    @Column(name = "fotoUrl")
    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clienteId", nullable = false)
    private OwnerEntity owner;
}