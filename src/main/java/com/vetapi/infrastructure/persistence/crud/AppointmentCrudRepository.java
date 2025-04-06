package com.vetapi.infrastructure.persistence.crud;

import com.vetapi.domain.entity.Appointment;
import com.vetapi.infrastructure.persistence.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface AppointmentCrudRepository extends JpaRepository<AppointmentEntity,Long> {


    List<AppointmentEntity> findByPetId(Long idPet);
    List<AppointmentEntity> findByDateTime(LocalDateTime date);
    List<AppointmentEntity> findByVeterinarianId(Long idVeterinarian);
}
