package com.vetapi.infrastructure.persistence.repository;

import com.vetapi.domain.entity.Pet;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.repository.PetRepository;
import com.vetapi.infrastructure.persistence.crud.CustomerCrudRepository;
import com.vetapi.infrastructure.persistence.crud.PetCrudRepository;
import com.vetapi.infrastructure.persistence.entity.CustomerEntity;
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
    private final CustomerCrudRepository customerCrudRepository;
    private final PetEntityMapper mapper;

    @Override
    public List<Pet> findAll() {
        return crudRepository.findByActiveTrue()
                .stream()
                .map(entity -> {
                    Pet pet = mapper.toPet(entity);
                    if (entity.getCustomer() != null) {
                        pet.setCustomerId(entity.getCustomer().getId());
                        pet.setCustomerName(entity.getCustomer().getName());
                    }
                    return pet;
                })
                .collect(Collectors.toList());
    }


    @Override
    public Optional<Pet> findById(Long id) {
        return crudRepository.findById(id).map(entity -> {
            Pet pet = mapper.toPet(entity);

            // Extraer solo la información necesaria del cliente
            if (entity.getCustomer() != null) {
                pet.setCustomerId(entity.getCustomer().getId());
                pet.setCustomerName(entity.getCustomer().getName());
            }

            return pet;
        });
    }

    @Override
    public List<Pet> findByCustomerId(Long customerId) {
        return crudRepository.findByCustomerIdAndActiveTrue(customerId)
                .stream()
                .map(entity -> {
                    Pet pet = mapper.toPet(entity);

                    if (entity.getCustomer() != null) {
                        pet.setCustomerId(entity.getCustomer().getId());
                        pet.setCustomerName(entity.getCustomer().getName());
                    }

                    return pet;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Pet> findBySpecies(String species) {
        return crudRepository.findBySpeciesAndActiveTrue(species)
                .stream()
                .map(entity -> {
                    Pet pet = mapper.toPet(entity);
                    if (entity.getCustomer() != null) {
                        pet.setCustomerId(entity.getCustomer().getId());
                        pet.setCustomerName(entity.getCustomer().getName());
                    }
                    return pet;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Pet> findByBreed(String breed) {
        return crudRepository.findByBreedAndActiveTrue(breed)
                .stream()
                .map(entity -> {
                    Pet pet = mapper.toPet(entity);
                    if (entity.getCustomer() != null) {
                        pet.setCustomerId(entity.getCustomer().getId());
                        pet.setCustomerName(entity.getCustomer().getName());
                    }
                    return pet;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Pet> findByBirthDateAfter(LocalDate date) {
        return crudRepository.findByBirthDateAfterAndActiveTrue(date)
                .stream()
                .map(entity -> {
                    Pet pet = mapper.toPet(entity);
                    if (entity.getCustomer() != null) {
                        pet.setCustomerId(entity.getCustomer().getId());
                        pet.setCustomerName(entity.getCustomer().getName());
                    }
                    return pet;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<Pet> findByNameContaining(String query) {
        return crudRepository.findByNameContainingAndActiveTrue(query)
                .stream()
                .map(entity -> {
                    Pet pet = mapper.toPet(entity);

                    if (entity.getCustomer() != null) {
                        pet.setCustomerId(entity.getCustomer().getId());
                        pet.setCustomerName(entity.getCustomer().getName());
                    }

                    return pet;
                })
                .collect(Collectors.toList());
    }


    @Override
    public Pet save(Pet pet) {
        PetEntity entity = mapper.toEntity(pet);

        if (pet.getCustomer() != null) {
            CustomerEntity customerEntity = customerCrudRepository.getReferenceById(pet.getCustomer().getId());
            entity.setCustomer(customerEntity);
        }

        PetEntity savedEntity = crudRepository.save(entity);

        Pet savedPet = mapper.toPet(savedEntity);

        if (savedEntity.getCustomer() != null) {
            savedPet.setCustomerId(savedEntity.getCustomer().getId());
            savedPet.setCustomerName(savedEntity.getCustomer().getName());
        }

        return savedPet;
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
    public boolean hasConsultations(Long petId) {
        return crudRepository.hasConsultations(petId);
    }

    @Override
    public int countVaccinations(Long petId) {
        return crudRepository.countVaccinationsByPetId(petId);
    }

    @Override
    public Pet update(Long id, Pet pet) {
        PetEntity entity = crudRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + id));


        entity.setName(pet.getName());
        entity.setSpecies(pet.getSpecies());
        entity.setBreed(pet.getBreed());
        entity.setGender(pet.getGender());
        entity.setBirthDate(pet.getBirthDate());
        entity.setWeight(pet.getWeight());
        entity.setPhotoUrl(pet.getPhotoUrl());

        PetEntity savedEntity = crudRepository.save(entity);
        Pet savedPet = mapper.toPet(savedEntity);

        if (savedEntity.getCustomer() != null) {
            savedPet.setCustomerId(savedEntity.getCustomer().getId());
            savedPet.setCustomerName(savedEntity.getCustomer().getName());
        }

        return savedPet;
    }


}