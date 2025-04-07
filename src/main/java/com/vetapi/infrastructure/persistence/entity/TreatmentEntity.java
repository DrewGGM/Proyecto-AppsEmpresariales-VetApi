package com.vetapi.infrastructure.persistence.entity;

import com.vetapi.infrastructure.persistence.entity.base.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "treatments")
@Getter
@Setter
@NoArgsConstructor
public class TreatmentEntity extends BaseJpaEntity {

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity pet;

    @ManyToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private ConsultationEntity consultation;

    @Column(name = "medication")
    private String medicine;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "completed")
    private boolean completed;

    @Column(name = "observations")
    private String observations;
}