package com.vetapi.domain.repository;

import com.vetapi.domain.entity.Pet;

import java.util.List;
import java.util.Optional;

//Interfaz de repositorio para la entidad Pet
public interface PetRepository {
    List<Pet> findAll();
    Optional<Pet> findById(Long id);
    List<Pet> findByOwnerId(Long ownerId);
    List<Pet> findBySpecies(String species);
    Pet save(Pet pet);
    void delete(Long id);
    boolean existsById(Long id);
}