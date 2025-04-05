package com.vetapi.infrastructure.persistence.crud;

import com.vetapi.infrastructure.persistence.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PetCrudRepository extends JpaRepository<PetEntity, Long> {

    List<PetEntity> findByActiveTrue();

    List<PetEntity> findByCustomerIdAndActiveTrue(Long customerId);

    List<PetEntity> findBySpeciesAndActiveTrue(String species);

    List<PetEntity> findByBreedAndActiveTrue(String breed);

    List<PetEntity> findByBirthDateAfterAndActiveTrue(LocalDate date);

    List<PetEntity> findByNameContainingAndActiveTrue(String name);

    @Query("SELECT COUNT(c) FROM ConsultationEntity c WHERE c.pet.id = :petId")
    int countConsultationsByPetId(@Param("petId") Long petId);

    @Query("SELECT COUNT(v) FROM VaccinationEntity v WHERE v.pet.id = :petId")
    int countVaccinationsByPetId(@Param("petId") Long petId);
}