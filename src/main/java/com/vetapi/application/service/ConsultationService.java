package com.vetapi.application.service;

import com.vetapi.application.dto.consultation.ConsultationCreateDTO;
import com.vetapi.application.dto.consultation.ConsultationDTO;
import com.vetapi.application.dto.consultation.ConsultationUpdateDTO;
import com.vetapi.application.mapper.ConsultationDTOMapper;
import com.vetapi.domain.entity.Consultation;
import com.vetapi.domain.entity.Pet;
import com.vetapi.domain.entity.User;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.repository.ConsultationRepository;
import com.vetapi.domain.repository.PetRepository;
import com.vetapi.domain.repository.UserRepository;
import com.vetapi.infrastructure.persistence.entity.ConsultationEntity;
import com.vetapi.infrastructure.persistence.entity.PetEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ConsultationService {
    private ConsultationRepository repository;
    private ConsultationDTOMapper mapper;
    private UserRepository userRepository;
    private PetRepository petRepository;

    public ConsultationDTO save(ConsultationCreateDTO consultation){
        Consultation consultation1= mapper.toConsultation(consultation);
        Optional<Pet> pet= petRepository.findById(consultation.getPetId());
        Optional<User> userV= userRepository.findById(consultation.getVeterinarianId());

        if (pet.isPresent() && userV.isPresent()){
            consultation1.setPet(pet.get());
            consultation1.setVeterinarian(userV.get());
            return mapper.toConsultationDto(repository.save(consultation1));
        }else {
            throw  new IllegalArgumentException("El pet o el veterinario no existen");
        }
    }
    public ConsultationDTO findById(Long id){
        return repository.findById(id)
                .map(mapper::toConsultationDto)
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found with ID: " + id));

    }
    public List<ConsultationDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toConsultationDto)
                .collect(Collectors.toList());
    }
    public List<ConsultationDTO> findByVeterinarian(Long idVeterinarian){
        return repository.findByVeterinarian(idVeterinarian)
                .stream()
                .map(mapper::toConsultationDto)
                .collect(Collectors.toList());
    }
    public List<ConsultationDTO> findByPet(Long idPet){
        return repository.findByPet(idPet)
                .stream()
                .map(mapper::toConsultationDto)
                .collect(Collectors.toList());
    }
    public List<ConsultationDTO>findByDate(LocalDate date){
        return repository.findByDate(date)
                .stream()
                .map(mapper::toConsultationDto)
                .collect(Collectors.toList());
    }
    public ConsultationDTO update(ConsultationUpdateDTO consultation, Long id){
        return null;
    }
    public boolean delete(Long id){
        Optional<Consultation> consultation= repository.findById(id);
        if (consultation.isPresent()){
            repository.delete(id);
            return true;
        }
        return false;
    }
}
