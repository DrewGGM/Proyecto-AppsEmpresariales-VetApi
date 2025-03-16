package com.vetapi.infrastructure.persistence.entity;

import com.vetapi.infrastructure.persistence.entity.base.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//Entidad JPA que representa un cliente/propietario en la base de datos
@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
public class OwnerEntity extends BaseJpaEntity {

    @Column(name = "nombre", nullable = false)
    private String name;

    @Column(name = "apellidos", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telefono", nullable = false)
    private String phone;

    @Column(name = "direccion")
    private String address;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetEntity> pets = new ArrayList<>();
}