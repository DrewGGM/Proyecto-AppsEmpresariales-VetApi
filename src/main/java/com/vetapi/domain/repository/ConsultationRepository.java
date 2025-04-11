package com.vetapi.domain.repository;

import com.vetapi.domain.entity.Consultation;

import java.io.CharArrayReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConsultationRepository {
    Consultation save(Consultation consultation);
    Optional<Consultation> findById(Long id);
    List<Consultation> findAll();
    List<Consultation> findByVeterinarian(Long idVeterinarian);
    List<Consultation> findByPet(Long idPet);
    List<Consultation>findByDate(LocalDate date);
    Consultation update(Consultation consultation, Long id);
    void delete(Long id);
}
