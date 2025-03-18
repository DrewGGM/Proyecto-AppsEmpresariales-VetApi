package com.vetapi.domain.repository;

import com.vetapi.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    // Obtiene todos los usuarios activos
    List<User> findAll();

    // Obtiene un usuario por su id
    Optional<User> findById(Long id);

    // Obtiene un usuario por su email
    Optional<User> findByEmail(String email);

    // Obtiene usuarios por rol
    List<User> findByRole(String role);

    // Obtiene usuarios que han accedido después de una fecha
    List<User> findByLastAccessAfter(LocalDateTime date);

    // Busca usuarios por nombre o apellido (búsqueda parcial)
    List<User> findByNameOrLastNameContaining(String query);

    // Guarda un usuario (crea o actualiza)
    User save(User user);

    // Elimina un usuario de forma lógica (cambio de estado activo)
    void delete(Long id);

    // Verifica si existe un usuario con el email indicado
    boolean existsByEmail(String email);

    // Actualiza la contraseña de un usuario
    boolean updatePassword(Long userId, String newPassword);

    // Obtiene la cantidad de consultas realizadas por un veterinario
    int countConsultations(Long userId);

    // Obtiene la cantidad de vacunaciones realizadas por un veterinario
    int countVaccinations(Long userId);
}