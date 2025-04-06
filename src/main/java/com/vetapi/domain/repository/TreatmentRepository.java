package com.vetapi.domain.repository;

import com.vetapi.domain.entity.Treatment;

import java.util.List;
import java.util.Optional;

public interface TreatmentRepository {
    Treatment save(Treatment treatment);
    Optional<Treatment> findById(Long id);
    List<Treatment> findAll();
    List<Treatment>findByPet(Long idPet);
    void delete(Long id);
    List<Treatment>findByConsultation(Long idConsultation);
    Treatment update(Treatment treatment, Long id);

    
}
