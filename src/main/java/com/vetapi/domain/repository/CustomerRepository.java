package com.vetapi.domain.repository;

import com.vetapi.domain.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    // Obtiene todos los clientes activos
    List<Customer> findAll();

    // Obtiene un cliente por su id
    Optional<Customer> findById(Long id);

    // Obtiene un cliente por su email
    Optional<Customer> findByEmail(String email);

    // Busca clientes por nombre o apellido (búsqueda parcial)
    List<Customer> findByNameOrLastNameContaining(String query);

    // Guarda un cliente (crea o actualiza)
    Customer save(Customer customer);

    // Elimina un cliente de forma lógica (cambio de estado activo)
    void delete(Long id);

    // Verifica si existe un cliente con el email indicado
    boolean existsByEmail(String email);

    // Obtiene la cantidad de mascotas de un cliente
    int countPets(Long customerId);
}