package com.vetapi.infrastructure.persistence.entity;

import com.vetapi.domain.entity.Pet;
import com.vetapi.infrastructure.persistence.entity.base.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "vacunacion")
@Getter
@Setter
@NoArgsConstructor
public class VaccinationEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascotaId", nullable = false)
    private PetEntity pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioId", nullable = false)
    private UsuarioEntity veterinarian;

    @Column(name = "tipoVacuna")
    private String vaccineType;

    @Column(name = "fechaAplicacion", nullable = false)
    private LocalDate dateApplication;

    @Column(name = "fechaProximaAplicacion")
    private LocalDate nextApplicationDate;

    @Column(name = "lote")
    private String batch;

    @Column(name = "observaciones")
    private String observations;
}
