package com.vetapi.infrastructure.persistence.entity;

import com.vetapi.domain.entity.Consultation;
import com.vetapi.domain.entity.Pet;
import com.vetapi.infrastructure.persistence.entity.base.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "cita")
@Getter
@Setter
@NoArgsConstructor
public class TreatmentEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascotaId", nullable = false)
    private PetEntity pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultaId", nullable = false)
    private Consultation consultation;

    @Column(name = "medicamento")
    private String medicine;

    @Column(name = "dosis")
    private String dose;

    @Column(name = "frecuencia")
    private String frequency;

    @Column(name = "fechaInicio")
    private LocalDate startDate;

    @Column(name = "fechaFin")
    private  LocalDate endDate;

    @Column(name = "completado")
    private  boolean filled;

    @Column(name = "observaciones")
    private  String observations;
}
