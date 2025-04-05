package com.vetapi.infrastructure.persistence.repository;

import com.vetapi.domain.entity.Pet;
import com.vetapi.domain.repository.PetRepository;
import com.vetapi.infrastructure.persistence.crud.PetCrudRepository;
import com.vetapi.infrastructure.persistence.entity.PetEntity;
import com.vetapi.infrastructure.persistence.mapper.PetEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PetRepositoryImpl implements PetRepository {

    private final PetCrudRepository crudRepository;
    private final PetEntityMapper mapper;

    @Override
    public List<Pet> findAll() {
        return crudRepository.findByActiveTrue()
                .stream()
                .map(mapper::toPet)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Pet> findById(Long id) {
        return crudRepository.findById(id).map(mapper::toPet);
    }

    @Override
    public List<Pet> findByCustomerId(Long customerId) {
        return crudRepository.findByCustomerIdAndActiveTrue(customerId)
                .stream()
                .map(mapper::toPet)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pet> findBySpecies(String species) {
        return crudRepository.findBySpeciesAndActiveTrue(species)
                .stream()
                .map(mapper::toPet)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pet> findByBreed(String breed) {
        return crudRepository.findByBreedAndActiveTrue(breed)
                .stream()
                .map(mapper::toPet)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pet> findByBirthDateAfter(LocalDate date) {
        return crudRepository.findByBirthDateAfterAndActiveTrue(date)
                .stream()
                .map(mapper::toPet)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pet> findByNameContaining(String query) {
        return crudRepository.findByNameContainingAndActiveTrue(query)
                .stream()
                .map(mapper::toPet)
                .collect(Collectors.toList());
    }

    @Override
    public Pet save(Pet pet) {
        PetEntity entity = mapper.toEntity(pet);

        // Set customer relationship if pet has a customer
        if (pet.getCustomer() != null) {
            // Assuming you have a customer entity mapper or can get the customer entity
            entity.setCustomer(crudRepository.getReferenceById(pet.getCustomer().getId()).getCustomer());
        }

        return mapper.toPet(crudRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        crudRepository.findById(id).ifPresent(entity -> {
            entity.setActive(false);
            crudRepository.save(entity);
        });
    }

    @Override
    public int countConsultations(Long petId) {
        return crudRepository.countConsultationsByPetId(petId);
    }

    @Override
    public int countVaccinations(Long petId) {
        return crudRepository.countVaccinationsByPetId(petId);
    }
}