package com.vetapi.infrastructure.persistence.crud;

import com.vetapi.domain.entity.Treatment;
import com.vetapi.infrastructure.persistence.entity.TreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentCrudRepository extends JpaRepository<TreatmentEntity,Long> {
    List<TreatmentEntity> findByConsultationId(Long consultationId);
    List<TreatmentEntity>findByPetId(Long petId);
}
