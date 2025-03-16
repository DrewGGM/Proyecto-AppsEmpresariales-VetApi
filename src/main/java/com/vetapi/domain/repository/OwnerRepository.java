package com.vetapi.domain.repository;

import com.vetapi.domain.entity.Owner;

import java.util.List;
import java.util.Optional;

//Interfaz de repositorio para la entidad Owner
public interface OwnerRepository {
    List<Owner> findAll();
    Optional<Owner> findById(Long id);
    List<Owner> findByNameContaining(String name);
    Owner save(Owner owner);
    void delete(Long id);
    boolean existsById(Long id);
    boolean existsByEmail(String email);
}