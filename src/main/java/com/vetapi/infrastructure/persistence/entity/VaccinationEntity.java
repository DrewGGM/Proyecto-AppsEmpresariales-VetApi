package com.vetapi.infrastructure.persistence.entity;

import com.vetapi.infrastructure.persistence.entity.base.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "vaccinations")
@Getter
@Setter
@NoArgsConstructor
public class VaccinationEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id", nullable = false)
    private UserEntity veterinarian;

    @Column(name = "vaccine_type")
    private String vaccineType;

    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;

    @Column(name = "next_application_date")
    private LocalDate nextApplicationDate;

    @Column(name = "lot_number")
    private String lotNumber;

    @Column(name = "observations")
    private String observations;

}