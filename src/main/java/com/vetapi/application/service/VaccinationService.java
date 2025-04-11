package com.vetapi.application.service;

import com.vetapi.application.dto.vaccination.VaccinationCreateDTO;
import com.vetapi.application.dto.vaccination.VaccinationDTO;
import com.vetapi.application.dto.vaccination.VaccinationUpdateDTO;
import com.vetapi.application.mapper.TreatmentDTOMapper;
import com.vetapi.application.mapper.VaccinationDTOMapper;
import com.vetapi.domain.entity.Pet;
import com.vetapi.domain.entity.Treatment;
import com.vetapi.domain.entity.User;
import com.vetapi.domain.entity.Vaccination;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.exception.InvalidDataException;
import com.vetapi.domain.repository.PetRepository;
import com.vetapi.domain.repository.TreatmentRepository;
import com.vetapi.domain.repository.UserRepository;
import com.vetapi.domain.repository.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccinationService {
private final VaccinationRepository repository;
private final VaccinationDTOMapper mapper;
private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Transactional
    public VaccinationDTO save(VaccinationCreateDTO vaccination){
Vaccination vaccination1=mapper.toVaccination(vaccination);
Optional<User> userV= userRepository.findById(vaccination.getVeterinarianId());
Optional<Pet> pet= petRepository.findById(vaccination.getPetId());
if (userV.isPresent() && pet.isPresent()){
    vaccination1.setPet(pet.get());
    vaccination1.setVeterinarian(userV.get());
    return mapper.toVaccinationDTO(repository.save(vaccination1));
}
        throw  new IllegalArgumentException("El pet o el user no existen");

    }
    public VaccinationDTO findById(Long id){
        return repository.findById(id)
                .map(mapper::toVaccinationDTO)
                .orElseThrow(() -> new EntityNotFoundException("Vaccination not found with ID: " + id));
    }
    public List<VaccinationDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toVaccinationDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public boolean delete(Long id){
        if (repository.existVaccination(id)){
            repository.delete(id);
            return true;
        }
        return false;
    }
    public List<VaccinationDTO> findByPet(Long petId){
        return repository.findByPet(petId)
                .stream()
                .map(mapper::toVaccinationDTO)
                .collect(Collectors.toList());
    }
    public List<VaccinationDTO> findByNextApplicationDate(LocalDate pending){
        return repository.findByNextApplicationDate(pending)
                .stream()
                .map(mapper::toVaccinationDTO)
                .collect(Collectors.toList());
    }
   public List<VaccinationDTO> findByDate(LocalDate date){
        return repository.findByDate(date)
                .stream()
                .map(mapper::toVaccinationDTO)
                .collect(Collectors.toList());
   }
    @Transactional
    public VaccinationDTO update(VaccinationUpdateDTO updateDTO, Long id) {
        Vaccination vaccination = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccination not found with ID: " + id));

        mapper.updateVaccinationFromDTO(updateDTO, vaccination);

        if (vaccination.getApplicationDate() != null &&
                vaccination.getApplicationDate().isAfter(LocalDate.now())) {
            throw new InvalidDataException("La fecha de aplicación no puede ser futura");
        }

        if (vaccination.getNextApplicationDate() != null &&
                vaccination.getApplicationDate() != null &&
                vaccination.getNextApplicationDate().isBefore(vaccination.getApplicationDate())) {
            throw new InvalidDataException("La fecha de próxima aplicación debe ser posterior a la fecha de aplicación");
        }

        return mapper.toVaccinationDTO(repository.save(vaccination));
    }
}
