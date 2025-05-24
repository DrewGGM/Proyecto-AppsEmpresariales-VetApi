package com.vetapi.infrastructure.persistence.entity;

import com.vetapi.infrastructure.persistence.entity.base.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseJpaEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(name = "last_access")
    private LocalDateTime lastAccess;

    @Column(name = "photo_url", length = 255)
    private String photoUrl;

    @Builder.Default
    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsultationEntity> consultations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VaccinationEntity> vaccinations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentEntity> appointments = new ArrayList<>();

}