package com.vetapi.infrastructure.persistence.repository;

import com.vetapi.domain.entity.Appointment;
import com.vetapi.domain.repository.AppointmentRepository;
import com.vetapi.domain.repository.PetRepository;
import com.vetapi.domain.repository.UserRepository;
import com.vetapi.infrastructure.persistence.crud.AppointmentCrudRepository;
import com.vetapi.infrastructure.persistence.crud.PetCrudRepository;
import com.vetapi.infrastructure.persistence.crud.UserCrudRepository;
import com.vetapi.infrastructure.persistence.entity.AppointmentEntity;
import com.vetapi.infrastructure.persistence.entity.PetEntity;
import com.vetapi.infrastructure.persistence.entity.UserEntity;
import com.vetapi.infrastructure.persistence.mapper.AppointmentEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AppointmentRepositoryImpl implements AppointmentRepository {
    private AppointmentCrudRepository crudRepository;
    private AppointmentEntityMapper mapper;
    private UserCrudRepository userCrudRepository;
    private PetCrudRepository petCrudRepository;
    @Override
    public Appointment save (Appointment appointment){
        AppointmentEntity  entity= mapper.toEntity(appointment);
        PetEntity petEntity = petCrudRepository.getReferenceById(appointment.getPet().getId());
        UserEntity userEntity= userCrudRepository.getReferenceById(appointment.getVeterinarian().getId());
        if (petEntity.isActive() && userEntity.isActive()) {
            entity.setPet(petEntity);
            entity.setVeterinarian(userEntity);
            return mapper.toAppointment(crudRepository.save(entity));
        }
        return null;
    }

    @Override
    public Optional<Appointment> findById(Long id){
         return crudRepository.findById(id)
                 .map(mapper::toAppointment);
    }
    @Override
    public List<Appointment> findAll(){
         return crudRepository.findAll()
                 .stream()
                .map(mapper::toAppointment)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByPet(Long idPet){
        return crudRepository.findByPetId(idPet)
                .stream()
                .map(mapper::toAppointment)
                .collect(Collectors.toList());
    }
    @Override
    public List<Appointment> findByDateTime(LocalDateTime date){
        return crudRepository.findByDateTime(date)
                .stream()
                .map(mapper :: toAppointment)
                .collect(Collectors.toList());
    }
    @Override
    public List<Appointment> findByVeterinarian(Long idVeterinarian){
        return crudRepository.findByVeterinarianId(idVeterinarian)
                .stream()
                .map(mapper::toAppointment)
                .collect(Collectors.toList());
    }
    @Override
    public void delete(Long id){
        crudRepository.deleteById(id);
    }
    @Override
    public Appointment update(Appointment appointment, Long id){
        Optional<AppointmentEntity> entity= crudRepository.findById(id);
        if(entity.isEmpty()){
        return null;
        }
       AppointmentEntity  entity1=entity.get();
        if (appointment.getObservations() != null){
        entity1.setObservations(appointment.getObservations());
        }
        if (appointment.getReason() != null){
            entity1.setReason(appointment.getReason());
        }
        if (appointment.getDateTime() != null){
            entity1.setDateTime(appointment.getDateTime());
        }
       return mapper.toAppointment(crudRepository.save(entity1)) ;
    }
}
