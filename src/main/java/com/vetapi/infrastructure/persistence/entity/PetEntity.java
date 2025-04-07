package com.vetapi.infrastructure.persistence.entity;

import com.vetapi.infrastructure.persistence.entity.base.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pets")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PetEntity extends BaseJpaEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(nullable = false, length = 50)
    private String species;

    @Column(length = 50)
    private String breed;

    @Column(length = 10)
    private String gender;

    @Column
    private Float weight;

    @Column(name = "photo_url", length = 255)
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Builder.Default
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsultationEntity> consultations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VaccinationEntity> vaccinations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentEntity> appointments = new ArrayList<>();
}