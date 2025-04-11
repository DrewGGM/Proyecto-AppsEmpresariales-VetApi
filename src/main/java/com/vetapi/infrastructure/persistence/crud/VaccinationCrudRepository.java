package com.vetapi.infrastructure.persistence.crud;

import com.vetapi.domain.entity.Vaccination;
import com.vetapi.infrastructure.persistence.entity.VaccinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccinationCrudRepository extends JpaRepository<VaccinationEntity,Long> {
    List<VaccinationEntity> findByPetId(Long petId);
    List<VaccinationEntity>findByNextApplicationDate(LocalDate date);
    List<VaccinationEntity> findByApplicationDate(LocalDate date);


}
