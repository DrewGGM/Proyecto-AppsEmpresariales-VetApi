package com.vetapi.domain.repository;

import com.vetapi.application.mapper.AppointmentDTOMapper;
import com.vetapi.domain.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {

    Appointment save (Appointment appointment);
    Optional<Appointment> findById(Long id);
    List<Appointment> findAll();
    List<Appointment> findByPet(Long idPet);
    List<Appointment> findByDateTime(LocalDateTime date);
    List<Appointment> findByVeterinarian(Long idVeterinarian);
    void delete(Long id);
    Appointment update(Appointment appointment, Long id);



}
