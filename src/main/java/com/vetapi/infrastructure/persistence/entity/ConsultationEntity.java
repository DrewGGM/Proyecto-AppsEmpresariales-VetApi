package com.vetapi.infrastructure.persistence.entity;

import com.vetapi.domain.entity.Pet;
import com.vetapi.infrastructure.persistence.entity.base.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "consulta")
@Getter
@Setter
@NoArgsConstructor
public class ConsultationEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascotaId", nullable = false)
    private PetEntity pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioId", nullable = false)
    private UserEntity veterinarian;

    @Column(name = "fecha")
    private LocalDateTime date;

    @JoinColumn(name = "motivo")
    private String reason;

    @JoinColumn(name = "diagnostico")
    private String diagnosis;

    @JoinColumn(name = "observaciones")
    private String observations;

}
