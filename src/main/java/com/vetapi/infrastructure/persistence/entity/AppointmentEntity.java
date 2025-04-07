package com.vetapi.infrastructure.persistence.entity;

import com.vetapi.infrastructure.persistence.entity.base.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
public class AppointmentEntity extends BaseJpaEntity {

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity pet;

    @ManyToOne
    @JoinColumn(name = "veterinarian_id", nullable = false)
    private UserEntity veterinarian;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "reason")
    private String reason;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "confirmed")
    private boolean confirmed;

    @Column(name = "observations")
    private String observations;
}