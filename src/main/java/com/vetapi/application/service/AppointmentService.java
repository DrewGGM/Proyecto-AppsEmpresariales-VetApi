package com.vetapi.application.service;

import com.vetapi.application.dto.appointment.AppointmentCreateDTO;
import com.vetapi.application.dto.appointment.AppointmentDTO;
import com.vetapi.application.dto.appointment.AppointmentListDTO;
import com.vetapi.application.dto.appointment.AppointmentUpdateDTO;
import com.vetapi.application.mapper.AppointmentDTOMapper;
import com.vetapi.domain.entity.Appointment;
import com.vetapi.domain.entity.Pet;
import com.vetapi.domain.entity.User;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.repository.AppointmentRepository;
import com.vetapi.domain.repository.PetRepository;
import com.vetapi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private AppointmentRepository repository;
    private AppointmentDTOMapper mapper;
   private PetRepository petRepository;
   private UserRepository userRepository;


    public AppointmentDTO save (AppointmentCreateDTO appointment){
        Appointment appointment1=mapper.toAppointment(appointment);
        Optional<Pet> pet=  petRepository.findById(appointment.getPetId());
        Optional<User> user =userRepository.findById(appointment.getVeterinarianId());

      if (pet.isPresent() && user.isPresent()){
          appointment1.confirm();
          appointment1.setVeterinarian(user.get());
          appointment1.setPet(pet.get());
          Appointment appointment2=  repository.save(appointment1);
          return  mapper.toAppointmentDTO(appointment2);
      }else {
          throw  new IllegalArgumentException("El pet o user no existen");
      }

    }
    public AppointmentDTO findById(Long id){
        Optional<Appointment>appointment =repository.findById(id);
     return appointment.map(mapper::toAppointmentDTO)
             .orElseThrow(() -> new EntityNotFoundException("Appointment not found with ID: " + id));
    }
    public List<AppointmentDTO> findAll(){
          return repository.findAll()
                  .stream()
                  .map(mapper::toAppointmentDTO)
                  .collect(Collectors.toList());
    }

    public List<AppointmentDTO> findByPet(Long idPet){
        return repository.findByPet(idPet)
                .stream()
                .map(mapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }
    public List<AppointmentDTO> findByDate(LocalDateTime date){
        return repository.findByDateTime(date)
                .stream()
                .map(mapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }
    public List<AppointmentDTO> findByVeterinarian(Long idVeterinarian){
        return repository.findByVeterinarian(idVeterinarian)
                .stream()
                .map(mapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }
   public boolean delete(Long id){
      Optional<Appointment> appointment= repository.findById(id);
        if (appointment.isPresent()){
            repository.delete(id);
            return true;
        }
        return false;
   }
    public AppointmentDTO update(AppointmentUpdateDTO appointment, Long id){
        Appointment appointment1 = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with ID: " + id));

        mapper.updateAppointmentFromDTO(appointment, appointment1);
        return mapper.toAppointmentDTO(repository.update(appointment1,id));
    }

}
