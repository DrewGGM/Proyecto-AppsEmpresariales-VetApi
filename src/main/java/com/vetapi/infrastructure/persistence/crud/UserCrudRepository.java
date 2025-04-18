package com.vetapi.infrastructure.persistence.crud;

import com.vetapi.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserCrudRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByActiveTrue();

    List<UserEntity> findByRoleAndActiveTrue(String role);

    List<UserEntity> findByLastAccessAfterAndActiveTrue(LocalDateTime date);

    List<UserEntity> findByNameContainingOrLastNameContainingAndActiveTrue(String name, String lastName);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(c) FROM ConsultationEntity c WHERE c.veterinarian.id = :veterinarianId")
    int countConsultationsByVeterinarianId(@Param("veterinarianId") Long veterinarianId);

    @Query("SELECT COUNT(v) FROM VaccinationEntity v WHERE v.veterinarian.id = :veterinarianId")
    int countVaccinationsByVeterinarianId(@Param("veterinarianId") Long veterinarianId);
}