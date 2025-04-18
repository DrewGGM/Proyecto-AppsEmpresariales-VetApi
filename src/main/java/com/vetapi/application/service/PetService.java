package com.vetapi.application.service;

import com.vetapi.application.dto.pet.PetCreateDTO;
import com.vetapi.application.dto.pet.PetDTO;
import com.vetapi.application.dto.pet.PetListDTO;
import com.vetapi.application.dto.pet.PetUpdateDTO;
import com.vetapi.application.mapper.PetDTOMapper;
import com.vetapi.domain.entity.Customer;
import com.vetapi.domain.entity.Pet;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.repository.CustomerRepository;
import com.vetapi.domain.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;
    private final PetDTOMapper mapper;

    public List<PetListDTO> findAll() {
        return mapper.toPetListDTOList(petRepository.findAll());
    }

    public PetDTO findById(Long id) {
        return petRepository.findById(id)
                .map(mapper::toPetDTO)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + id));
    }

    public List<PetListDTO> findByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new EntityNotFoundException("Customer not found with ID: " + customerId);
        }
        return mapper.toPetListDTOList(petRepository.findByCustomerId(customerId));
    }


    public List<PetListDTO> findBySpecies(String species) {
        return mapper.toPetListDTOList(petRepository.findBySpecies(species));
    }

    public List<PetListDTO> findByBreed(String breed) {
        return mapper.toPetListDTOList(petRepository.findByBreed(breed));
    }

    public List<PetListDTO> findByBirthDateAfter(LocalDate date) {
        return mapper.toPetListDTOList(petRepository.findByBirthDateAfter(date));
    }

    public List<PetListDTO> findByNameContaining(String query) {
        return mapper.toPetListDTOList(petRepository.findByNameContaining(query));
    }

    @Transactional
    public PetDTO create(PetCreateDTO createDTO) {
        Customer customer = customerRepository.findById(createDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + createDTO.getCustomerId()));

        if (createDTO.getWeight() != null && createDTO.getWeight() <= 0) {
            throw new IllegalArgumentException("El peso debe ser un número positivo.");
        }

        if (createDTO.getBirthDate() != null && createDTO.getBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }

        Pet pet = mapper.toPet(createDTO);
        pet.setCustomer(customer);

        return mapper.toPetDTO(petRepository.save(pet));
    }

    @Transactional
    public PetDTO update(Long id, PetUpdateDTO updateDTO) {
        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + id));

        mapper.updatePetFromDTO(updateDTO, existingPet);

        return mapper.toPetDTO(petRepository.update(id, existingPet));
    }


    @Transactional
    public void delete(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + id));

        // Verificar si tiene consultas
        if (petRepository.hasConsultations(id)) {
            throw new IllegalStateException("No se puede eliminar la mascota porque tiene consultas asociadas.");
        }

        petRepository.delete(id);
    }


    public int countConsultations(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + petId));

        return petRepository.countConsultations(petId);
    }

    public int countVaccinations(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + petId));

        return petRepository.countVaccinations(petId);
    }
}