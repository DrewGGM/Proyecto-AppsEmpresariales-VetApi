package com.vetapi.domain.repository;

import com.vetapi.domain.entity.Vaccination;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VaccinationRepository {

    Vaccination save(Vaccination vaccination);
    Optional<Vaccination> findById(Long id);
    List<Vaccination> findAll();
    void delete(Long id);
    boolean existVaccination(Long id);
    List<Vaccination> findByPet(Long petId);
    List<Vaccination> findByNextApplicationDate(LocalDate pending);
    List<Vaccination> findByDate(LocalDate date);

}
