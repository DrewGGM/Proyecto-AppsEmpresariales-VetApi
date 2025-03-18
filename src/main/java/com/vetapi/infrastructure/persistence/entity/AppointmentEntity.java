package com.vetapi.infrastructure.persistence.entity;

import com.vetapi.domain.entity.Pet;
import com.vetapi.infrastructure.persistence.entity.base.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cita")
@Getter
@Setter
@NoArgsConstructor
public class AppointmentEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascotaId", nullable = false)
    private PetEntity pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioId", nullable = false)
    private UserEntity veterinarian;

    @Column(name = "fechaHora", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "motivo")
    private String reason;

    @Column(name = "estado", nullable = false)
    private String status;

    @Column(name = "confirmada")
    private boolean confirmed;

    @Column(name = "observaciones")
    private String observations;
}
