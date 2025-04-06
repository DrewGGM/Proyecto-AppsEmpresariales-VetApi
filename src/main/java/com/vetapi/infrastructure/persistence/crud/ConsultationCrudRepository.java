package com.vetapi.infrastructure.persistence.crud;

import com.vetapi.domain.entity.Consultation;
import com.vetapi.infrastructure.persistence.entity.ConsultationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.module.Configuration;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsultationCrudRepository  extends JpaRepository<ConsultationEntity, Long> {
    List<ConsultationEntity> findByVeterinarianId(Long idVeterinarian);
    List<ConsultationEntity> findByPetId(Long idPet);
    List<ConsultationEntity>findByDate(LocalDate date);
}
