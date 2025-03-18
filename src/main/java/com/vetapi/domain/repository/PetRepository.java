package com.vetapi.domain.repository;

import com.vetapi.domain.entity.Pet;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PetRepository {

    // Obtiene todas las mascotas activas
    List<Pet> findAll();

    // Obtiene una mascota por su id
    Optional<Pet> findById(Long id);

    // Obtiene las mascotas de un cliente
    List<Pet> findByCustomerId(Long customerId);

    // Obtiene mascotas por especie
    List<Pet> findBySpecies(String species);

    // Obtiene mascotas por raza
    List<Pet> findByBreed(String breed);

    // Obtiene mascotas nacidas después de una fecha
    List<Pet> findByBirthDateAfter(LocalDate date);

    // Busca mascotas por nombre (búsqueda parcial)
    List<Pet> findByNameContaining(String query);

    // Guarda una mascota (crea o actualiza)
    Pet save(Pet pet);

    // Elimina una mascota de forma lógica (cambio de estado activo)
    void delete(Long id);

    // Obtiene la cantidad de consultas de una mascota
    int countConsultations(Long petId);

    // Obtiene la cantidad de vacunas de una mascota
    int countVaccinations(Long petId);
}